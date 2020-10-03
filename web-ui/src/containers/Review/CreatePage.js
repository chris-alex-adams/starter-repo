import React, { Component } from 'react';
import SearchBar from '../../components/SearchBar.js';
import {Container, Col, Row, Table, Button, Form, FormGroup, Label, Input, FormText, Alert } from 'reactstrap';
import CoinInfoRow from '../../components/CoinInfoRow.js';
import { withCookies, Cookies } from 'react-cookie';

class ReviewPage extends React.Component {

  constructor(props) {
    const { cookies } = props;

    super(props);
    this.state = {
      isLoaded: false,
      currentCoinSymbol: "",
      items: [],
      reviewTitle: "",
      reviewDesc: "",
      reviewRecommend: "",
      accessToken: cookies.get('accessToken') || '',
      error : "",
      message: ""
    };
    this.coinSearch = this.coinSearch.bind(this);
    this.getCoin = this.getCoin.bind(this);
    this.formSubmit = this.formSubmit.bind(this);
    this.handleTitleChange = this.handleTitleChange.bind(this);
    this.handleDescChange = this.handleDescChange.bind(this);
    this.handleRecommendChange = this.handleRecommendChange.bind(this);
    this.insertReview = this.insertReview.bind(this);
  }

  coinSearch(coinSymbol) {
    this.setState({ currentCoinSymbol: coinSymbol });
    this.getCoin(coinSymbol);
  }

  formSubmit(e) {
    e.preventDefault();
    const reviewRequest = {
      title: this.state.reviewTitle,
      description: this.state.reviewDesc,
      recommendation: this.state.reviewRecommend,
      coinSymbol: this.state.currentCoinSymbol
    };

    this.insertReview(reviewRequest);
  }

  handleTitleChange(e) {
    this.setState({ reviewTitle: e.target.value });
    console.debug("title change: " + e.target.value);
  }

  handleDescChange(e) {
    this.setState({ reviewDesc: e.target.value });
    console.debug("desc change: " + e.target.value);
  }

  handleRecommendChange(e) {
    this.setState({ reviewRecommend: e.target.value });
    console.debug("Recommendation change: " + e.target.value);
  }

  getCoin(symbol) {
    fetch("https://coinmarketpoll.com/apidata//coin/" + symbol)
      .then(res => res.json())
      .then(
        (result) => {
          this.setState({
            isLoaded: true,
            items: result
          });
        },
        // Note: it's important to handle errors here
        // instead of a catch() block so that we don't swallow
        // exceptions from actual bugs in components.
        (error) => {
          this.setState({
            isLoaded: true,
            error
          });
        console.error('Error:', error);
        }
      )
  }

  insertReview(review) {
    const { history} = this.props;
    fetch('https://coinmarketpoll.com/apidata//review/create', {
    method: 'post',
    headers: {
        Accept: "application/json",
        "Content-Type": "application/json",
        'Access-Control-Allow-Origin': '*',
        'Authorization': 'Bearer ' + this.state.accessToken
    },
    body: JSON.stringify(review)
    }).then(res => res.json())
    .then((response) => {
      if(response) {
        if(response.id) {
          console.debug("200 response " + JSON.stringify(response));
          this.setState({
            message: "Success!"
          });
          history.push("/review/"+response.id);
        }else if(response.status === 403){
          console.debug("403 error " + JSON.stringify(response));
          this.setState({
            error: response
          });
        } else {
          console.debug("other error " + JSON.stringify(response));
          this.setState({
            error: "Whoops. Something went wrong."
          });
        }
      }
      console.debug("200 response " + JSON.stringify(response));
      return response
    });

  }

  render() {
    const {items, isLoaded, error, message} = this.state;
    console.debug("items: " + JSON.stringify(items));
      return (
        <Container className="createPage">
        <Row>
          <Col>
            { error &&
            <div className="errorAlert">
              <Alert color="danger" >
                {error.message}
              </Alert>
            </div>
            }
            { message &&
              <div>
                <Alert color="success">
                  {message}
                </Alert>
              </div>
            }
          </Col>
        </Row>
          <Row className="coinReviewCreateTitle">
            <Col sm="12" md={{ size: 6, offset: 3 }}>
            <h1 className="createReviewSearchTitle">Coin Review</h1>
            <div className="createReviewSearch">
              <SearchBar onChangeValue={this.coinSearch}/>
            </div>
            </Col>
            </Row>
            {items && isLoaded ?
            <Row  className="reviewRow">
              <Col>
              <Table className="coinTable">
              <thead>
                <tr>
                <th className="tableCell">Logo</th>
                <th className="tableCell">Name</th>
                <th className="tableCell">Price</th>
                <th className="tableCell">Reviews Last Day</th>
                </tr>
              </thead>
              <tbody>
                <CoinInfoRow data = {items} />
              </tbody>
              </Table>
              <Form onSubmit={this.formSubmit}>
                <FormGroup>
                  <Label for="reviewDesc">Your review</Label>
                  <Input type="textarea" name="reviewDesc" id="reviewDesc" placeholder="" onChange={this.handleDescChange}/>
                </FormGroup>
                <FormGroup tag="fieldset" row>
                  <legend className="col-form-label col-sm-2">Recommendation</legend>
                  <Col>
                    <FormGroup check>
                      <Label check>
                        <Input type="radio" name="radio2" value="SELL" onChange={this.handleRecommendChange}/>{' '}
                        Sell
                      </Label>
                    </FormGroup>
                    <FormGroup check>
                      <Label check>
                        <Input type="radio" name="radio2" value="HOLD" onChange={this.handleRecommendChange} />{' '}
                        Hold
                      </Label>
                    </FormGroup>
                    <FormGroup check disabled>
                      <Label check>
                        <Input type="radio" name="radio2" value="BUY" onChange={this.handleRecommendChange}/>{' '}
                        Buy
                      </Label>
                    </FormGroup>
                  </Col>
                </FormGroup>
                <Button>Submit your review</Button>
              </Form>
              </Col>
            </Row> : null }
        </Container>
      );
    }
}

export default withCookies(ReviewPage);

import React, { Component } from 'react';
import {Container, Col, Row, Media, Table, Button} from 'reactstrap';
import CoinInfoRow from '../../components/CoinInfoRow.js';
import CoinUtil from '../../utils/CoinUtil.js';
import ReviewFeed from '../../components/ReviewFeed.js';
import {Link} from 'react-router-dom';
import { withCookies, Cookies } from 'react-cookie';
import { withRouter } from "react-router-dom";

const Arrow = ({direction, ...props}) => (
  <svg viewBox="0 0 28 12" {...props}>
    <polyline
      points={
        direction === 'up' ?
        "0.595,11.211 14.04,1.245 27.485,11.211" :
        "27.485,0.803 14.04,10.769 0.595,0.803"
      }
    />
  </svg>
)

class ViewPage extends Component {

  constructor(props) {
    super(props);

    const { cookies } = props;

    this.state = {
      review: null,
      error: null,
      count: 0,
      reviewLoaded: false,
      coinLoaded: false,
      coinInfo: null,
      relatedReviewsIsLoaded: false,
      relatedReviews: [],
      userReview: null,
      accessToken: cookies.get('accessToken') || ''
    };

    this.getReview = this.getReview.bind(this);
    this.getCoinInfo = this.getCoinInfo.bind(this);
    this.getReviews = this.getReviews.bind(this);
    this.sendReviewVote = this.sendReviewVote.bind(this);
    this.getReviewVote = this.getReviewVote.bind(this);
  }

  getReview(id) {
    fetch("https://coinmarketpoll.com/apidata//review/" + id)
      .then(res => res.json())
      .then(
        (result) => {
          this.setState({
            reviewLoaded: true,
            review: result
          });
          this.getCoinInfo(this.state.review.coinSymbol);
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

  getReviews(coinSymbol) {
    fetch("https://coinmarketpoll.com/apidata//reviews/hot/0")
      .then(res => res.json())
      .then(
        (result) => {
          this.setState({
            relatedReviewsIsLoaded: true,
            relatedReviews: result
          });
        },
        // Note: it's important to handle errors here
        // instead of a catch() block so that we don't swallow
        // exceptions from actual bugs in components.
        (error) => {
          this.setState({
            relatedReviewsIsLoaded: true,
            error
          });
        console.error('Error:', error);
        }
      )
  }

  getReviewVote(reviewId) {
    fetch("https://coinmarketpoll.com/apidata//reviewVote/"+reviewId, {
      method: 'get',
      headers: {
          Accept: "application/json",
          "Content-Type": "application/json",
          'Access-Control-Allow-Origin': '*',
          'Authorization': 'Bearer ' + this.state.accessToken
      }
    }).then(res => res.json())
      .then(
        (result) => {
          this.setState({
            userReview: result
          });
        },
        // Note: it's important to handle errors here
        // instead of a catch() block so that we don't swallow
        // exceptions from actual bugs in components.
        (error) => {
          this.setState({
            error
          });
        }
      )
  }

  getCoinInfo(symbol) {
    fetch("https://coinmarketpoll.com/apidata//coin/" + symbol)
      .then(res => res.json())
      .then(
        (result) => {
          this.setState({
            coinLoaded: true,
            coinInfo: result
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

  sendReviewVote(type) {
    const reviewRequest = {
      type: type,
      reviewId: this.state.review.id,
      coinSymbol: this.state.coinInfo.manifestCoin.symbol
    };

    if(this.state.accessToken == '') {
      this.props.history.push("/login");
    }

    fetch('https://coinmarketpoll.com/apidata//reviewVote/create', {
    method: 'post',
    headers: {
        Accept: "application/json",
        "Content-Type": "application/json",
        'Access-Control-Allow-Origin': '*',
        'Authorization': 'Bearer ' + this.state.accessToken
    },
    body: JSON.stringify(reviewRequest)
    }).then(function (response) {
        return response.json(); //response.json() is resolving its promise. It waits for the body to load
    }).then(function (json) {
        return json;
    });

    var newReview = this.state.review;
    var newUserReview = this.state.userReview;
    if(this.state.userReview) {
      if(type == "UP" && this.state.userReview.type == "DOWN") {
        newReview.reviewVoteSummary.upCount += 1;
        newReview.reviewVoteSummary.downCount -= 1;
        newUserReview.type = "UP";
      } else if(type == "DOWN" && this.state.userReview.type == "UP"){
        newReview.reviewVoteSummary.downCount += 1;
        newReview.reviewVoteSummary.upCount -= 1;
        newUserReview.type = "DOWN";
      } else if(type == "DOWN" && this.state.userReview.type == "DOWN"){
        newReview.reviewVoteSummary.downCount -= 1;
        newUserReview.type = "NONE";
      } else if(type == "UP" && this.state.userReview.type == "UP"){
        newReview.reviewVoteSummary.upCount -= 1;
        newUserReview.type = "NONE";
      } else if(type == "UP" && this.state.userReview.type == "NONE"){
          newReview.reviewVoteSummary.upCount += 1;
          newUserReview.type = "UP";
      } else if(type == "DOWN" && this.state.userReview.type == "NONE"){
          newReview.reviewVoteSummary.downCount += 1;
          newUserReview.type = "DOWN";
      }
    }

    this.setState({
      review: newReview,
      userReview: newUserReview
    });
  }

  render() {
    const { error, reviewLoaded, coinLoaded, review, coinInfo, count, userReview } = this.state;
    var isUserReviewUp = false;
    var isUserReviewDown = false;
    var isUserReviewNone = false;
    if(userReview && userReview.type == "UP") {
      isUserReviewUp = true;
    } else if(userReview && userReview.type == "DOWN") {
      isUserReviewDown = true;
    } else {
      isUserReviewNone = true;
    }
    if (error) {
        return <div>Error: {error.message}</div>;
      } else if (!reviewLoaded || !coinLoaded) {
        return <div>Loading...</div>;
      } else if(review && coinInfo) {
      return (
        <Container>
            <Row  className="reviewRow">
            <Col xs="12" md="3" lg="3" style={{textAlign:'center'}}>
            <img className="reviewPageIcon" src={"data:image/svg+xml;base64,"+coinInfo.manifestCoin.iconImage}/>
            <h2 className="reviewPageText"><Link onClick={this.forceUpdate} to={"/reviewCoin/" + coinInfo.manifestCoin.symbol}>{coinInfo.manifestCoin.name}</Link></h2>
            <h3 className="reviewPageText">{coinInfo.manifestCoin.symbol}</h3>
            <h3 className="reviewPageText">{CoinUtil.formatMoney(coinInfo.coinSummary.vwapPriceUsd)} <a className="CurrencyTag">USD</a></h3>
            <div className="votingDiv">
              <h4 className="vote">{review.reviewVoteSummary.upCount} likes</h4>
              <div>
              <h4 className="vote">{review.reviewVoteSummary.downCount} dislikes</h4>
              </div>
              {isUserReviewUp &&
                <div>
                  <Arrow
                  direction="up"
                  className="vote__arrow vote__arrow--up greenArrow"
                  onClick={() => this.sendReviewVote("UP")}
                  />
                  <h4> Vote </h4>
                  <Arrow
                  direction="down"
                  className="vote__arrow vote__arrow--down"
                  onClick={() => this.sendReviewVote("DOWN")}
                  />
                </div>
              }
              {isUserReviewDown &&
                <div>
                  <Arrow
                  direction="up"
                  className="vote__arrow vote__arrow--up"
                  onClick={() => this.sendReviewVote("UP")}
                  />
                  <h4> Vote </h4>
                  <Arrow
                  direction="down"
                  className="vote__arrow vote__arrow--down redArrow"
                  onClick={() => this.sendReviewVote("DOWN")}
                  />
                </div>
              }
              {isUserReviewNone &&
                <div>
                  <Arrow
                  direction="up"
                  className="vote__arrow vote__arrow--up"
                  onClick={() => this.sendReviewVote("UP")}
                  />
                  <h4> Vote </h4>
                  <Arrow
                  direction="down"
                  className="vote__arrow vote__arrow--down"
                  onClick={() => this.sendReviewVote("DOWN")}
                  />
                </div>
              }
              </div>
            </Col>

              <Col>
              <Media className="reviewPageReview">
                <Media body className="reviewPageDescription">
                  <Media heading>
                    {review.recommendation + " " + review.coinTitle + " "}
                    <a className="reviewPageUserInfo"> - Posted by <a>{review.userId}</a> 4 hours ago</a>
                  </Media>
                  {review.description}
                </Media>
              </Media>
              </Col>

            </Row>
            <Row>
            </Row>
            <Row className="reviewPageRelatedReviews">
            <h2 className="reviewPageRelatedReviewsTitle">Hot Reviews</h2>
              <Col xs="12" md="9" lg="12">
                <ReviewFeed items = {this.state.relatedReviews} isLoaded = {this.state.relatedReviewsIsLoaded} error = {this.state.error}/>
              </Col>
            </Row>
        </Container>
      );
      } else {
        return null
      }
    }

    componentDidMount() {
      this.getReview(this.props.match.params.id);
      this.getReviews("");
      this.getReviewVote(this.props.match.params.id);
    }

}

export default withRouter(withCookies(ViewPage));

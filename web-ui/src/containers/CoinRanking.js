import React, { Component } from 'react';
import CoinTable from '../components/CoinTable.js';
import ReviewFeed from '../components/ReviewFeed.js';
import CoinSearchBar from '../components/CoinSearchBar.js';
import { withCookies, Cookies } from 'react-cookie';
import {Container, Col, Row, Button } from 'reactstrap';
import { instanceOf } from 'prop-types';

class CoinRanking extends React.Component {
  static propTypes = {
    cookies: instanceOf(Cookies).isRequired
  };

  constructor(props) {
    const { cookies } = props;

    super(props);
    this.state = {
      error: null,
      isNewReviews: true,
      isHotReviewDaily: false,
      isHotReviewWeekly: false,
      isHotReviewMonthly: false,
      isLoaded: false,
      currentPageReview: 0,
      currentPageCoinTable: 0,
      items: []
    };

    this.getNewReviews = this.getNewReviews.bind(this);
    this.getHotReviews = this.getHotReviews.bind(this);
    this.getTopWeekly = this.getTopWeekly.bind(this);
    this.getTopMonthly = this.getTopMonthly.bind(this);
    this.updatePageChange = this.updatePageChange.bind(this);
    this.setPrevPage = this.setPrevPage.bind(this);
    this.setNextPage = this.setNextPage.bind(this);
    this.refreshReviews = this.refreshReviews.bind(this);
    this.setReviewDay = this.setReviewDay.bind(this);
    this.setReviewWeek = this.setReviewWeek.bind(this);
    this.setReviewMonth = this.setReviewMonth.bind(this);
    this.setReviewNew = this.setReviewNew.bind(this);
  }

  setPrevPage() {
    var val = this.state.currentPageReview;
    val -= 1;
    this.setState({currentPageReview: val});
    this.refreshReviews(val);
  }

  setNextPage() {
    var val = this.state.currentPageReview;
    val += 1;
    this.setState({currentPageReview: val});
    this.refreshReviews(val);
  }

  updatePageChange(pageNum) {
    this.setState({currentPageCoinTable: pageNum});
  }

  getHotReviews(pageNum) {
    this.setState({isHotReviewDaily: true, isNewReviews: false, isHotReviewWeekly: false, isHotReviewMonthly: false});
    fetch("https://coinmarketpoll.com/apidata//reviews/hot/" + pageNum)
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

  getTopWeekly(pageNum) {
    this.setState({isHotReviewWeekly: true, isNewReviews: false, isHotReviewDaily: true, isHotReviewMonthly: false});
    fetch("https://coinmarketpoll.com/apidata//reviews/weekly/" + pageNum)
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

  getTopMonthly(pageNum) {
    this.setState({isHotReviewMonthly: true, isNewReviews: false, isHotReviewDaily: false, isHotReviewWeekly: false});
    fetch("https://coinmarketpoll.com/apidata//reviews/monthly/" + pageNum)
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

  refreshReviews(pageNum) {
    if(this.state.isNewReviews) {
      this.getNewReviews(pageNum);
    } else if(this.state.isHotReviewDaily) {
      this.getHotReviews(pageNum);
    } else if(this.state.isHotReviewWeekly) {
      this.getTopWeekly(pageNum);
    } else if(this.state.isHotReviewMonthly) {
      this.getTopMonthly(pageNum);
    }
    window.scrollTo(0, 0);
    console.debug("current page: " +this.state.currentPageReview);
  }

  setReviewNew() {
    this.setState({isHotReviewMonthly: false, isNewReviews: true, isHotReviewDaily: false, isHotReviewWeekly: false, currentPageReview: 0});
    this.getNewReviews(0);
  }

  setReviewDay() {
    this.setState({isHotReviewMonthly: false, isNewReviews: false, isHotReviewDaily: true, isHotReviewWeekly: false, currentPageReview: 0});
    this.getHotReviews(0);
  }

  setReviewWeek() {
    this.setState({isHotReviewMonthly: false, isNewReviews: false, isHotReviewDaily: false, isHotReviewWeekly: true, currentPageReview: 0});
    this.getTopWeekly(0);
  }

  setReviewMonth() {
    this.setState({isHotReviewMonthly: true, isNewReviews: false, isHotReviewDaily: false, isHotReviewWeekly: false, currentPageReview: 0});
    this.getTopMonthly(0);
  }

  render() {
    const { currentPageReview } = this.state;
    return (
      <Container className="homePageContainer">
        <Row>
          <Col xs="9">
            <CoinSearchBar/>
          </Col>
        </Row>
        <Row>
          <Col xs="12" lg="9">
          <div className="coinRankingHeader">
            <h3 className="homePageReviewHeader">Cryptocurrency Reviews</h3>
            <div className="coinRankingButtons">
              <Button color="secondary" onClick={this.setReviewNew}>New</Button>
              <Button color="secondary" onClick={this.setReviewDay}>Hot</Button>
              <Button color="secondary" onClick={this.setReviewWeek}>Top Weekly</Button>
              <Button color="secondary" onClick={this.setReviewMonth}>Top Monthly</Button>
            </div>
          </div>
            <ReviewFeed items = {this.state.items} isLoaded = {this.state.isLoaded} error = {this.state.error}/>
            <div>
              <Button color="secondary" onClick={this.setPrevPage} disabled={currentPageReview <= 0}>Previous</Button>
              <Button color="secondary" onClick={this.setNextPage}  disabled={this.state.items.length < 15}>Next</Button>
            </div>
          </Col>
          <Col xs="12" lg="3">
            <h4 className="coinTableTitle">Hot Coins</h4>
            <CoinTable onPageChange={this.updatePageChange}/>
          </Col>
          <Col xs="12" lg="12">

          </Col>

        </Row>
      </Container>
    );
  }

  getNewReviews(pageNum) {
    this.setState({isNewReviews: true, isHotReviewDaily: false, isHotReviewWeekly: false, isHotReviewMonthly: false});
    fetch("https://coinmarketpoll.com/apidata//reviews/new/" + pageNum)
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

  componentDidMount() {
    this.getNewReviews(this.state.currentPageReview);
  }
}

export default withCookies(CoinRanking);

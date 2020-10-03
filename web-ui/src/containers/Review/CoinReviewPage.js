import React, { Component } from 'react';
import {Container, Col, Row, Media, Table, Button} from 'reactstrap';
import CoinInfoRow from '../../components/CoinInfoRow.js';
import CoinUtil from '../../utils/CoinUtil.js';
import ReviewFeed from '../../components/ReviewFeed.js';

export default class ViewPage extends Component {

  constructor(props) {
    super(props);
    this.state = {
      items: null,
      error: null,
      reviewLoaded: false,
      coinLoaded: false,
      coinInfo: null,
      currentPageReview: 0,
      isNewReviews: true,
      isHotReviewDaily: false,
      isHotReviewWeekly: false,
      isHotReviewMonthly: false
    };

    this.getCoinInfo = this.getCoinInfo.bind(this);
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

  getHotReviews(coinSymbol, pageNum) {
    this.setState({isHotReviewDaily: true, isNewReviews: false, isHotReviewWeekly: false, isHotReviewMonthly: false});
    fetch("https://coinmarketpoll.com/apidata//reviews/" + coinSymbol + "/monthly/" + pageNum)
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

  getTopWeekly(coinSymbol, pageNum) {
    this.setState({isHotReviewWeekly: true, isNewReviews: false, isHotReviewDaily: true, isHotReviewMonthly: false});
    fetch("https://coinmarketpoll.com/apidata//reviews/" + coinSymbol + "/weekly/" + pageNum)
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

  getTopMonthly(coinSymbol, pageNum) {
    this.setState({isHotReviewMonthly: true, isNewReviews: false, isHotReviewDaily: false, isHotReviewWeekly: false});
    fetch("https://coinmarketpoll.com/apidata//reviews/" + coinSymbol + "/monthly/" + pageNum)
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
      this.getNewReviews(this.props.match.params.symbol, pageNum);
    } else if(this.state.isHotReviewDaily) {
      this.getHotReviews(this.props.match.params.symbol, pageNum);
    } else if(this.state.isHotReviewWeekly) {
      this.getTopWeekly(this.props.match.params.symbol, pageNum);
    } else if(this.state.isHotReviewMonthly) {
      this.getTopMonthly(this.props.match.params.symbol, pageNum);
    }
    window.scrollTo(0, 0);
    console.debug("current page: " +this.state.currentPageReview);
  }

  setReviewNew() {
    this.setState({isHotReviewMonthly: false, isNewReviews: true, isHotReviewDaily: false, isHotReviewWeekly: false, currentPageReview: 0});
    this.getNewReviews(this.props.match.params.symbol, 0);
  }

  setReviewDay() {
    this.setState({isHotReviewMonthly: false, isNewReviews: false, isHotReviewDaily: true, isHotReviewWeekly: false, currentPageReview: 0});
    this.getHotReviews(this.props.match.params.symbol, 0);
  }

  setReviewWeek() {
    this.setState({isHotReviewMonthly: false, isNewReviews: false, isHotReviewDaily: false, isHotReviewWeekly: true, currentPageReview: 0});
    this.getTopWeekly(this.props.match.params.symbol, 0);
  }

  setReviewMonth() {
    this.setState({isHotReviewMonthly: true, isNewReviews: false, isHotReviewDaily: false, isHotReviewWeekly: false, currentPageReview: 0});
    this.getTopMonthly(this.props.match.params.symbol, 0);
  }

  getNewReviews(coinSymbol, pageNum) {
    this.setState({isNewReviews: true, isHotReviewDaily: false, isHotReviewWeekly: false, isHotReviewMonthly: false});
    fetch("https://coinmarketpoll.com/apidata//reviews/" + coinSymbol + "/new/" + pageNum)
      .then(res => res.json())
      .then(
        (result) => {
          this.setState({
            reviewLoaded: true,
            items: result
          });
        },
        // Note: it's important to handle errors here
        // instead of a catch() block so that we don't swallow
        // exceptions from actual bugs in components.
        (error) => {
          this.setState({
            reviewLoaded: false,
            error
          });
        console.error('Error:', error);
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

  render() {
    const { error, reviewLoaded, coinLoaded, coinInfo, currentPageReview, items} = this.state;
    if (error) {
        return <div>Error: {error.message}</div>;
      } else if (!reviewLoaded || !coinLoaded) {
        return <div>Loading...</div>;
      } else if(items && coinInfo) {
      return (
        <Container>
            <Row  className="CoinReviewPageRow">
            <Col xs="12">
              <h1><img className="reviewPageIconLeft" src={"data:image/svg+xml;base64,"+coinInfo.manifestCoin.iconImage}/><a>{coinInfo.manifestCoin.name}</a></h1>
            </Col>
            </Row>
            <Row  className="CoinReviewPageRow">
              <Col xs="12">
                <h3>Symbol: </h3>
                <h1>{coinInfo.manifestCoin.symbol}</h1>
              </Col>
            </Row>
            <Row  className="CoinReviewPageRow">
              <Col xs="12">
                <h3>Price per coin: </h3>
                <h1><a className="">{CoinUtil.formatMoney(coinInfo.coinSummary.vwapPriceUsd)} <a className="CurrencyTag">USD</a></a></h1>
              </Col>
            </Row>
            <Row  className="CoinReviewPageRow">
              <Col xs="12">
              <p className="CoinReviewDescription">
              {coinInfo.manifestCoin.description}
              </p>
              </Col>
            </Row>

            <Row className="reviewPageRelatedReviews">
              <h2 className="reviewPageRelatedReviewsTitle">Reviews</h2>
            </Row>
            <Row className="reviewFeedItem">
              <div className="coinRankingButtons">
                <Button color="secondary" onClick={this.setReviewNew}>New</Button>
                <Button color="secondary" onClick={this.setReviewDay}>Hot</Button>
                <Button color="secondary" onClick={this.setReviewWeek}>Top Weekly</Button>
                <Button color="secondary" onClick={this.setReviewMonth}>Top Monthly</Button>
              </div>
              <Col xs="12" md="9" lg="12">
                <ReviewFeed items = {this.state.items} isLoaded = {this.state.reviewLoaded} error = {this.state.error}/>
              </Col>
            </Row>
            <Row>
              <Col>
                <div>
                  {currentPageReview > 0 &&
                    <Button color="secondary" onClick={this.setPrevPage} disabled={currentPageReview <= 0}>Previous</Button>
                  }
                  {this.state.items.length > 14 &&
                  <Button color="secondary" onClick={this.setNextPage}  disabled={this.state.items.length < 15}>Next</Button>
                  }
                </div>
              </Col>
            </Row>

        </Container>
      );
      } else {
        return null
      }
    }

    componentDidMount() {
      this.getNewReviews(this.props.match.params.symbol, 0);
      this.getCoinInfo(this.props.match.params.symbol);
    }

}

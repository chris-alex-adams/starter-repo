import React, { Component } from 'react';

import CoinUtil from '../utils/CoinUtil.js';
import SearchBar from '../components/SearchBar.js';
import {Container, Col, Row, Button } from 'reactstrap';
import { withCookies, Cookies } from 'react-cookie';
import { withRouter } from "react-router-dom";

class CoinSearchBar extends React.Component {

  constructor(props) {
    const { cookies } = props;

    super(props);
    this.state = {
      isLoaded: false,
      currentCoinSymbol: "",
    };
    this.coinSearch = this.coinSearch.bind(this);
    this.isLoaded = this.isLoaded.bind(this);
  }

  coinSearch(coinSymbol) {
    const { history} = this.props;
    history.push("/reviewCoin/"+coinSymbol);
  }

  isLoaded(load) {
    this.setState({
      isLoaded: load
    });
  }

  render() {
    const {isLoaded } = this.state;
    return (
      <div>
        <Container>
          <Row className="coinReviewCreateTitle">
            <Col sm="12" md={{ size: 8, offset: 3 }}>
            <div className="createReviewSearch">
              <h3 className = "mainSearch">Coin Search</h3>
              <SearchBar isLoaded={this.isLoaded} onChangeValue={this.coinSearch}/>
            </div>
            </Col>
          </Row>
        </Container>
      </div>
    );
  }
}

export default withRouter(withCookies(CoinSearchBar));

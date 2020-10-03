import React, { Component } from 'react';
import {Spinner } from 'reactstrap';
import Review from '../components/ReviewFeedItem.js';

export default class CoinTable extends React.Component {

  render() {
    const { error, isLoaded, items } = this.props;
    if (error) {
      return <div>Error: {error.message}</div>;
    } else if (!isLoaded) {
      return <Spinner style={{ width: '3rem', height: '3rem' }}/>;
    } else if (items.length == 0) {
      return <div className="reviewFeed"><h6>There are no reviews yet...</h6> </div>;
    } else {
      return (
        <div className="reviewFeed">

          {items.map((item, i) => <Review key = {i}
          data = {item} />)}
        </div>

      );
    }
  }
}

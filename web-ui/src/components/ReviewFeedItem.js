import React, { Component } from 'react';
import {Container, Col, Row, Media, Table} from 'reactstrap';
import CoinUtil from '../utils/CoinUtil.js';
import { Route, Link} from 'react-router-dom';
import Upvote from 'react-upvote';

export default class Review extends React.Component {
  constructor(props) {
    super(props);

    this.state = {
      postUpvote: 0
    };


    this.cropText = this.cropText.bind(this);
    this.isLoggedIn = this.isLoggedIn.bind(this);
  }

  cropText(text) {
    const croppedText = text.slice(0,400);
    if(text.length > 400) {
      return croppedText + "...";
    }
    return croppedText;
  }

  isLoggedIn() {
    return true;
  }

  render() {
     return (
       <Row>

       <Col xs="1" md="1"className="likesText">
        <h5>{this.props.data.reviewVoteSummary.upCount}</h5>
        <h5>likes</h5>
       </Col>
       <Col xs="1" md="1"className="likesText">
        <h5>{this.props.data.reviewVoteSummary.downCount}</h5>
        <h5>dislikes</h5>
       </Col>
       <Col xs="8" md="8" className="review">
         <Media >
           <Media left href="#">
             <Media className="coinIcon" object src={"data:image/svg+xml;base64," + this.props.data.image} alt="Image" />
           </Media>
           <Media body className="reviewDescription">
             <Media heading>
               <Link onClick={this.forceUpdate} to={"/review/" + this.props.data.id}>{this.props.data.recommendation + " " + this.props.data.coinTitle}</Link>
             </Media>
             {this.cropText(this.props.data.description)}
             <div>
             <p className="reviewCreatedBy"> Posted {CoinUtil.secondsToHms(this.props.data.ageInSeconds)} ago by <a>{this.props.data.userId}</a></p>
             </div>

           </Media>
         </Media>
         </Col>
       </Row>
     );
  }


}

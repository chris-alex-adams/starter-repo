import React, { Component, Linking } from 'react';
import {Container, Col, Row, Media, Table} from 'reactstrap';
export default class FooterPanel extends React.Component {

  constructor(props) {
    super(props);
  }

  render() {
    return (
      <div>

        <footer className="footer">
            <a className="socialLink" href='/contact'>Contact</a>
            <a className="socialLink" href='/policy'>Policy</a>
            <a className="socialLink" target="_blank" href='https://www.facebook.com/Coin-Market-Poll-112913226754512/'>Facebook</a>
            <a className="socialLink" target="_blank" href='https://twitter.com/coinmarketpoll'>Twitter</a>
            Data provided by <a href="https://coinranking.com/" target="_blank" >Coinranking</a>
        </footer>
      </div>
    );
  }

}

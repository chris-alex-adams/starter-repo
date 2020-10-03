import React, { Component } from 'react';
import logo from './logo.svg';
import './App.css';
import HomeNavBar from '../components/HomeNavBar.js';
import CoinTable from '../components/CoinTable.js';
import FooterPanel from '../components/FooterPanel.js';
import { withCookies} from 'react-cookie';

class Home extends React.Component {
  render() {
    return (
      <div className="site">
      <HomeNavBar />
      <FooterPanel />
      </div>
    );
  }
}

export default withCookies(Home);

import React, { Component } from 'react';

import CoinUtil from '../utils/CoinUtil.js';
import {Link} from 'react-router-dom';

export default class CoinInfoRow extends React.Component {

  render() {
    if(this.props.data.manifestCoin) {
     return (
        <tr className = "coinTableRow">
     <td className = "coinTableCell"><img className="coinIcon" src={"data:image/svg+xml;base64,"+this.props.data.manifestCoin.iconImage} /></td>
           <td className = "coinTableCell"><Link onClick={this.forceUpdate} to={"/reviewCoin/" + this.props.data.manifestCoin.symbol}>{this.props.data.manifestCoin.name}</Link></td>
           <td className="coinTablePrice coinTableCell">{CoinUtil.formatMoney(this.props.data.coinSummary.vwapPriceUsd)}</td>
     <td className = "coinTableCell">{this.props.data.reviewsLastDay}</td>
        </tr>
     );
   } else {
     return (<div> Issue loading coin information. Please contact the admin. </div>);
   }
  }
}

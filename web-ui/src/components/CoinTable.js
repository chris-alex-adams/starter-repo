import React, { Component } from 'react';
import { Table, Row, Button, Spinner } from 'reactstrap';
import CoinInfoRow from '../components/CoinInfoRow.js';
import PagnationBar from '../components/PagnationBar.js';

export default class CoinTable extends React.Component {
  constructor(props) {
    super(props);
    this.state = {
      error: null,
      isLoaded: false,
      fullItems: [],
      currentItems: [],
      currentPage: 0,
      pageSize: 10
    };

    this.handleClick = this.handleClick.bind(this);
    this.callDataApi = this.callDataApi.bind(this);
    this.setNewItems = this.setNewItems.bind(this);
  }

  handleClick(e, index) {
    e.preventDefault();
    console.debug("current page: " + index);
    this.props.updatePageChange(index);
    this.setState({
      currentPage: index
    });
    this.setNewItems(index);
  }

  setNewItems(currentPage) {
    const {pageSize} = this.state;
    var newItems = this.state.fullItems.slice((currentPage * pageSize), ((currentPage + 1) * pageSize));
    console.debug("slice start index: " + (currentPage * pageSize));
    console.debug("slice end index: " + (currentPage + 1) * pageSize);
    this.setState({
      currentItems: newItems
    })
  }

  callDataApi(pageNumber) {
    fetch("https://coinmarketpoll.com/apidata//coins/orderByReviews")
      .then(res => res.json())
      .then(
        (result) => {
          this.setState({
            isLoaded: true,
            fullItems: result
          });
          this.setNewItems(this.state.currentPage);
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
  const { error, isLoaded, items, currentPage, currentItems } = this.state;
  if (error) {
      return <div>Error: {error.message}</div>;
    } else if (!isLoaded) {
      return <Spinner style={{ width: '3rem', height: '3rem' }}/>;
    } else if(currentItems) {
    return (
      <div>
        <div className="coinTable">
            <Table>

            <thead className = "CoinTableHead">
              <tr>
              <th>Logo</th>
              <th>Name</th>
              <th>Price</th>
              <th>Reviews Today</th>
              </tr>

            </thead>
            <tbody>
              {currentItems.map((item, i) => <CoinInfoRow key = {i}
              data = {item} />)}
            </tbody>
            </Table>
            {/*
              <PagnationBar className="coinTablePageBar" itemsPerPage={10} totalCoinsCount={currentItems.length} currentPage={this.state.currentPage} onChangeValue={this.handleClick}/>
             */}
        </div >
      </div>

  );
  } else {
    return null
  }
  }

  componentDidMount() {
    this.callDataApi(this.state.currentPage);
  }

}

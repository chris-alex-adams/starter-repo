import React from 'react';
import { Pagination, PaginationItem, PaginationLink } from 'reactstrap';

export default class PagnationBar extends React.Component {

  constructor() {
    super();

    this.state = {
      currentPage: 0,
      totalCoins: 0,
      pageCount: 0,
      error: null
    };

  }

  componentWillReceiveProps(newProps) {
    this.setState({currentPage: newProps.currentPage,
                  totalCoins: newProps.totalCoinsCount});

    if(newProps.totalCoinsCount > 0) {
      this.state.pagesCount = newProps.totalCoinsCount/25;
    }
  }

  componentDidMount() {


  }

  handleClick(e, index) {

    e.preventDefault();

    this.setState({
      currentPage: index
    });

    this.props.onChangeValue(e, index);

  }

  render() {
    const { currentPage } = this.state;

    return (
      <Pagination className="coinTablePageBar" aria-label="Coin Table Navigation">
      <PaginationItem disabled={currentPage <= 0}>
      <PaginationLink onClick={e => this.handleClick(e, currentPage - 1)}
      previous
      href="#" >Previous</PaginationLink>
      </PaginationItem>
      {/*
        {[...Array(this.pagesCount)].map((page, i) =>
        <PaginationItem active={i === currentPage } key={i}>
        <PaginationLink onClick={e => this.handleClick(e, i)} href="#">
        {i + 1}
        </PaginationLink>
        </PaginationItem>
      )}
      */}
      <PaginationItem className="coinTablePageNext10" disabled={currentPage >= this.state.pagesCount - 1}>
      <PaginationLink onClick={e => this.handleClick(e, currentPage + 1)}
      next
      href="#">Next</PaginationLink>
      </PaginationItem>
      </Pagination>
    );
  }
}

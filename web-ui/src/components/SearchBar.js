import React, { Component } from 'react';
import {Form, FormGroup, Input} from 'reactstrap';
import { Spinner } from 'reactstrap';
import Autosuggest from 'react-autosuggest';
import './SearchBar.css';

export default class SearchBar extends Component {
  constructor() {
    super();

    this.state = {
      value: '',
      suggestions: [],
      coinInfo: [],
      isLoaded: false
    };

    this.escapeRegexCharacters = this.escapeRegexCharacters.bind(this);
    this.getSuggestions = this.getSuggestions.bind(this);
    this.getSuggestionValue = this.getSuggestionValue.bind(this);
    this.renderSuggestion = this.renderSuggestion.bind(this);
    this.getAllCoins = this.getAllCoins.bind(this);
  }

  escapeRegexCharacters(str) {
    return str.replace(/[.*+?^${}()|[\]\\]/g, '\\$&');
  }

  getSuggestions(value) {
    const escapedValue = this.escapeRegexCharacters(value.trim());

    if (escapedValue === '') {
      return [];
    }

    const regex = new RegExp('^' + escapedValue, 'i');
    var filteredItems = this.state.coinInfo.filter(coinInfo => regex.test(coinInfo.manifestCoin.symbol)).concat(this.state.coinInfo.filter(coinInfo => regex.test(coinInfo.manifestCoin.name)));
    return Array.from(new Set(filteredItems));
  }

  getSuggestionValue(suggestion) {
    return suggestion.manifestCoin.symbol;
  }

  renderSuggestion(suggestion) {
    return (
      /* <span><img className="coinIcon" src={"data:image/svg+xml;base64,"+suggestion.manifestCoin.iconImage}/>{suggestion.manifestCoin.name}</span> */
      <span>{suggestion.manifestCoin.name}</span>
    );
  }

  onChange = (event, { newValue, method }) => {
    if(newValue == "" || newValue) {
      this.setState({
        value: newValue
      });
    }

    if(method == "click") {
      this.props.onChangeValue(newValue);
    }

  };

  onSuggestionsFetchRequested = ({ value }) => {
    this.setState({
      suggestions: this.getSuggestions(value)
    });
  };

  onSuggestionsClearRequested = () => {
    this.setState({
      suggestions: []
    });
  };

  getAllCoins() {
    fetch("https://coinmarketpoll.com/apidata/coins/")
      .then(res => res.json())
      .then(
        (result) => {
          this.setState({
            isLoaded: true,
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

  componentDidMount() {
    this.getAllCoins();
  }

  render() {
    const { value, suggestions, isLoaded} = this.state;
    const inputProps = {
      placeholder: "Search",
      value,
      onChange: this.onChange
    };

    if(isLoaded) {
      return (
        <Autosuggest
          suggestions={suggestions}
          onSuggestionsFetchRequested={this.onSuggestionsFetchRequested}
          onSuggestionsClearRequested={this.onSuggestionsClearRequested}
          getSuggestionValue={this.getSuggestionValue}
          renderSuggestion={this.renderSuggestion}
          inputProps={inputProps} />
      );
    } else {
      return <Spinner style={{ width: '3rem', height: '3rem' }} />
    }
  }
}

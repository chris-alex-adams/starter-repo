import React, { Component } from 'react';
import {
  Collapse,
  Navbar,
  NavbarToggler,
  NavbarBrand,
  Nav,
  NavItem,
  NavLink,
  UncontrolledDropdown,
  DropdownToggle,
  DropdownMenu,
  DropdownItem } from 'reactstrap';

import ReviewPage from '../containers/Review/CreatePage.js';
import CoinRanking from '../containers/CoinRanking.js';
import Home from '../containers/Home.js';
import { Route, Switch, Redirect } from 'react-router-dom'
import SearchBar from '../components/SearchBar.js';
import ViewPage from '../containers/Review/ViewPage.js';
import LoginPage from '../containers/User/LoginPage.js';
import SignUpPage from '../containers/User/SignUpPage.js';
import EditProfile from '../containers/User/EditProfile.js';
import CoinReviewPage from '../containers/Review/CoinReviewPage.js'
import Policy from '../containers/Policy.js'
import Contact from '../containers/Contact.js'
import { withCookies, Cookies } from 'react-cookie';
import logo from '../coinmarketpoll.png';

class HomeNavBar extends React.Component {
  constructor(props) {
    super(props);

    const { cookies } = props;

    this.state = {
      isOpen: false,
      accessToken: cookies.get('accessToken') || '',
      username: '',
      isLoggedIn: false
    };

    this.privateRoute = this.privateRoute.bind(this);
    this.getUsername = this.getUsername.bind(this);
    this.logout = this.logout.bind(this);
    this.toggle = this.toggle.bind(this);
  }

  toggle() {
    this.setState({
      isOpen: !this.state.isOpen
    });
  }

  getUsername(token) {
    console.debug("getting username in homenavbar");
    const that = this;
    var jwtObject = new Object();
    jwtObject.accessToken = token;
    console.debug("jwtObject: " + JSON.stringify(jwtObject));

    var username = fetch('https://coinmarketpoll.com/apidata//auth/username', {
    method: 'post',
    headers: {
        Accept: "application/json",
        "Content-Type": "application/json",
        'Access-Control-Allow-Origin': '*'
    },
    body: JSON.stringify(jwtObject)
    }).then(function (response) {
        return response.json(); //response.json() is resolving its promise. It waits for the body to load
    }).then(function (responseData) {
        return JSON.stringify(responseData);
    }).then(function (responseData) {
        console.debug("response: " + JSON.stringify(responseData));
        if(responseData) {
          that.setState({ isLoggedIn: true, username: responseData })
        } else {
          that.setState({ isLoggedIn: false})
        }

        return responseData;
    });
  }

  render() {
    const {isLoggedIn} = this.state;
    return (
      <div className="navbardiv">
        <Navbar color="white" light expand="md" className="homePageNavBar">
        <div className="navbar-brand">

          <NavbarBrand className="mx-auto" href="/" >
            <img className="mainLogo"src={logo}/>
          </NavbarBrand>
        </div>
          <NavbarToggler onClick={this.toggle} />
          <Collapse isOpen={this.state.isOpen} navbar>
            <Nav className="ml-auto navbaritems" navbar>
            <NavItem>

            </NavItem>
            <NavItem>
              <NavLink className="nav-link-white" href="/">Coin Rankings</NavLink>
            </NavItem>
            { isLoggedIn === true &&
              <NavItem>
                <NavLink className="nav-link-white" href="/reviews/submit">Submit Review</NavLink>
              </NavItem>
            }
            { isLoggedIn === false &&
              <NavItem>
                <NavLink className="nav-link-white" href="/login">Submit Review</NavLink>
              </NavItem>
            }
            { isLoggedIn === true &&
              <NavItem>
                <NavLink className="nav-link-white" href="/profile">Profile</NavLink>
              </NavItem>
            }
            { isLoggedIn === true &&
              <NavItem>
                <NavLink className="nav-link-white" href="/" onClick={this.logout}>Logout</NavLink>
              </NavItem>
            }
            { isLoggedIn === false &&
              <NavItem>
                <NavLink className="nav-link-white" href="/signup">Signup</NavLink>
              </NavItem>
            }
            { isLoggedIn === false &&
              <NavItem>
                <NavLink className="nav-link-white" href="/login">Login</NavLink>
              </NavItem>
            }
            </Nav>
          </Collapse>
        </Navbar>
        <div>

          <Route exact path="/" render={() => (<CoinRanking cookies={this.props.cookies}/>)}/>
          { isLoggedIn === true ?
          <this.privateRoute path='/reviews/submit' authed={this.state.isLoggedIn} component={ReviewPage} /> :
          <Route exact path="/reviews/submit" component={LoginPage} />
          }

          <Route exact path="/review/:id" component={ViewPage} />
          <Route exact path="/reviewCoin/:symbol" component={CoinReviewPage} />
          <Route exact path="/login" component={LoginPage} />
          <Route exact path="/profile" component={EditProfile} />
          <Route exact path="/signup" component={SignUpPage} />
          <Route exact path="/policy" component={Policy} />
          <Route exact path="/contact" component={Contact} />
        </div>

      </div>
    );
  }

  logout() {
    console.debug("you are logged out")
    const { cookies } = this.props;
    cookies.set('accessToken', "", { path: '/' });
    this.setState({
      isLoggedIn: false,
      username: ""
    });

  }

  privateRoute ({component: Component, authed,  ...rest}) {

    return (
      <Route
        {...rest}
        render={(props) => authed === true
          ? <Component {...props} />
          : <Redirect to={{pathname: '/login', state: {from: props.location}}} />}
      />
    )
  }

  componentWillUpdate(nextProps, nextState) {
    if (nextState.isLoggedIn == false && this.props.cookies.get('accessToken')) {
      this.getUsername(this.props.cookies.get('accessToken'));
    }
  }

  componentDidMount() {
    if(this.state.accessToken !== '') {
        console.debug("attempting to get username");
      this.getUsername(this.state.accessToken);
    } else {
      console.debug("Access token is empty");
    }
  }

}

export default withCookies(HomeNavBar);

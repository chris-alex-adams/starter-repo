import React, { Component } from 'react';
import SearchBar from '../../components/SearchBar.js';
import {Container, Col, Row, Table, Button, Form, FormGroup, Label, Input, FormText, Alert } from 'reactstrap';
import CoinInfoRow from '../../components/CoinInfoRow.js';
import { withCookies, Cookies } from 'react-cookie';
import { instanceOf } from 'prop-types';
import { withRouter } from "react-router-dom";

class LoginPage extends React.Component {

  static propTypes = {
    cookies: instanceOf(Cookies).isRequired
  };

  constructor(props) {
    const { cookies } = props;

    super(props);
    this.state = {
      username: "",
      password: "",
      accessToken: "",
      tokenLoaded: false,
      redirect: false,
      error: ""
    };

    this.loginSubmit = this.loginSubmit.bind(this);
    this.handlePasswordChange = this.handlePasswordChange.bind(this);
    this.handleUsernameChange = this.handleUsernameChange.bind(this);
  }

  loginSubmit(e) {
    e.preventDefault();
    var loginRequest = new Object();
    loginRequest.usernameOrEmail = this.state.username;
    loginRequest.password = this.state.password;
    var response = this.login(loginRequest);


  }

  handleUsernameChange(e) {
    e.preventDefault();
    this.setState({ username: e.target.value });
  }

  handlePasswordChange(e) {
    e.preventDefault();
    this.setState({ password: e.target.value });
  }

  componentWillUpdate(nextProps, nextState) {
    if (nextState.redirect == true && this.state.redirect == false) {
      this.props.history.push("/");
    }
  }

  login(loginRequest) {
    const { cookies, history} = this.props;
    var response = fetch("https://coinmarketpoll.com/apidata//auth/signin", {
    method: 'post',
    headers: {
        Accept: "application/json",
        "Content-Type": "application/json",
        'Access-Control-Allow-Origin': '*'
    },
    body: JSON.stringify(loginRequest)
    }).then(res => res.json())
    .then((json) => {
      if(json.accessToken){
        cookies.set('accessToken', json.accessToken, { path: '/' });
        this.setState({
          redirect: true
        });
        this.props.history.push("/");
        return json.status;
      } else {
        this.setState({
          error: json.message
        });
      }
      return json.status;
    });
    return response;
  }

  render() {
      const {error} = this.state;
      return (
        <Container>
          <Row >
            <Col sm="12" md={{ size: 6, offset: 3 }}>
              {error &&
                <div className="errorAlert">
                  <Alert color="danger" >
                    {error}
                  </Alert>
                </div>}
              <Form onSubmit={this.loginSubmit}>
                <h2> Login </h2>
                <FormGroup>
                  <Label for="reviewDesc">Username</Label>
                  <Input type="textfield" name="username" id="reviewDesc" placeholder="Satoshi" onChange={this.handleUsernameChange}/>
                </FormGroup>
                <FormGroup>
                  <Label for="reviewDesc">Password</Label>
                  <Input type="password" name="password" id="reviewDesc" placeholder="" onChange={this.handlePasswordChange}/>
                </FormGroup>
                <Button>Login</Button>
              </Form>
              </Col>
            </Row>
        </Container>
      );
    }
}

export default withRouter(withCookies(LoginPage));

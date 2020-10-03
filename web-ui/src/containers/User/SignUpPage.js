import React, { Component } from 'react';
import SearchBar from '../../components/SearchBar.js';
import {Container, Col, Row, Table, Button, Form, FormGroup, Label, Input, FormText, Alert } from 'reactstrap';
import CoinInfoRow from '../../components/CoinInfoRow.js';
import { withCookies, Cookies } from 'react-cookie';
import { instanceOf } from 'prop-types';
import { withRouter } from "react-router-dom";

class SignUpPage extends React.Component {

  static propTypes = {
    cookies: instanceOf(Cookies).isRequired
  };

  constructor(props) {
    const { cookies } = props;

    super(props);
    this.state = {
      username: "",
      password: "",
      email: "",
      accessToken: "",
      tokenLoaded: false,
      redirect: false,
      error: ""
    };

    this.signupSubmit = this.signupSubmit.bind(this);
    this.handlePasswordChange = this.handlePasswordChange.bind(this);
    this.handleEmailChange = this.handleEmailChange.bind(this);
    this.handleUsernameChange = this.handleUsernameChange.bind(this);
  }

  signupSubmit(e) {
    e.preventDefault();
    var signupRequest = new Object();
    signupRequest.username = this.state.username;
    signupRequest.email = this.state.email;
    signupRequest.password = this.state.password;
    var response = this.signup(signupRequest);
  }

  handleUsernameChange(e) {
    e.preventDefault();
    this.setState({ username: e.target.value });
  }

  handleEmailChange(e) {
    e.preventDefault();
    this.setState({ email: e.target.value });
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

  signup(signupRequest) {
    const { cookies, history} = this.props;
    var response = fetch("https://coinmarketpoll.com/apidata//auth/signup", {
    method: 'post',
    headers: {
        Accept: "application/json",
        "Content-Type": "application/json",
        'Access-Control-Allow-Origin': '*'
    },
    body: JSON.stringify(signupRequest)
    }).then(res => res.json())
    .then((json) => {
      console.debug("res : " + JSON.stringify(json));
      if(json.accessToken){
        cookies.set('accessToken', json.accessToken, { path: '/' });
        history.push("/");
      } else {
        this.setState({
          error: json.message
        });
      }
      return json.accessToken;
    });
    return response;
  }

  render() {
      const {error} = this.state;
      return (
        <Container>
          <Row >
            <Col sm="12" md={{ size: 6, offset: 3 }}>
              <Form onSubmit={this.signupSubmit}>
                <h2> Sign Up </h2>
                {error &&
                  <div className="errorAlert">
                    <Alert color="danger" >
                      {error}
                    </Alert>
                  </div>}
                <FormGroup>
                  <Label for="userName">Username</Label>
                  <Input type="textfield" name="username" id="username" placeholder="Satoshi" onChange={this.handleUsernameChange}/>
                </FormGroup>
                <FormGroup>
                  <Label for="email">Email</Label>
                  <Input type="email" name="email" id="email" placeholder="" onChange={this.handleEmailChange}/>
                </FormGroup>
                  <FormGroup>
                    <Label for="password">Password</Label>
                    <Input type="password" name="password" id="password" placeholder="" onChange={this.handlePasswordChange}/>
                  </FormGroup>
                <Button>Sign Up</Button>
              </Form>
              </Col>
            </Row>
        </Container>
      );
    }
}

export default withRouter(withCookies(SignUpPage));

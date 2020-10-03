import React, { Component } from 'react';
import SearchBar from '../../components/SearchBar.js';
import {Container, Col, Row, Table, Button, Form, FormGroup, Label, Input, FormText } from 'reactstrap';
import CoinInfoRow from '../../components/CoinInfoRow.js';
import { withCookies, Cookies } from 'react-cookie';
import { instanceOf } from 'prop-types';
import { withRouter } from "react-router-dom";

class EditProfile extends React.Component {

  static propTypes = {
    cookies: instanceOf(Cookies).isRequired
  };

  constructor(props) {
    const { cookies } = props;

    super(props);
    this.state = {
      accessToken: "",
      toggleEdit: false,
      accessToken: cookies.get('accessToken') || '',
      originalUsername: "",
      originalEmail: "",
      username: "",
      email: "",
      password: "",
      oldPassword: "",
      response: ""
    };

    this.submit = this.submit.bind(this);
    this.updateSubmit = this.updateSubmit.bind(this);
    this.toggleEdit = this.toggleEdit.bind(this);
    this.getUserInformation = this.getUserInformation.bind(this);
    this.handleEmailChange = this.handleEmailChange.bind(this);
    this.handleUsernameChange = this.handleUsernameChange.bind(this);
    this.handlePasswordChange = this.handlePasswordChange.bind(this);
    this.handleOldPasswordChange = this.handleOldPasswordChange.bind(this);
  }

  handleEmailChange(value) {
    this.setState({
      email: value.replace(" ", "")
    })
  }

  handleUsernameChange(value) {
    this.setState({
      username: value.replace(" ", "")
    })
  }

  handlePasswordChange(value) {
    this.setState({
      password: value
    })
  }

  handleOldPasswordChange(value) {
    this.setState({
      oldPassword: value
    })
  }

    toggleEdit() {
      const toggleEdit = this.state.toggleEdit;
      this.setState({
        toggleEdit: !toggleEdit,
        username: this.state.originalUsername,
        email: this.state.originalEmail,
        password: "",
        oldPassword: ""
      });
    }

    componentDidMount() {
      this.getUserInformation(this.state.accessToken);
    }

    getUserInformation(token) {
      const { history} = this.props;
      const request = {
        accessToken: token
      };

      console.debug("request = " + JSON.stringify(request))

      fetch('https://coinmarketpoll.com/apidata//auth/user', {
      method: 'post',
      headers: {
          Accept: "application/json",
          "Content-Type": "application/json",
          'Access-Control-Allow-Origin': '*',
          'Authorization': 'Bearer ' + this.state.accessToken
      },
      body: JSON.stringify(request)
      })
      .then(res => res.json())
      .then((response) => {
        if(response) {
          if(response.username) {
            this.setState({
              username: response.username,
              email: response.email,
              originalUsername: response.username,
              originalEmail: response.email
            });
          }
        }
        console.debug("200 response " + JSON.stringify(response));
        return response
      });
    }

    submit() {
      const request = {
        oldUsername: this.state.originalUsername,
        username: this.state.username,
        email: this.state.email,
        newPassword: this.state.password,
        oldPassword: this.state.oldPassword
      };

      this.updateSubmit(request);

    }

    updateSubmit(updateRequest) {
      const { cookies, history} = this.props;
      var response = fetch("https://coinmarketpoll.com/apidata//auth/updateUser", {
      method: 'post',
      headers: {
          Accept: "application/json",
          "Content-Type": "application/json",
          'Access-Control-Allow-Origin': '*'
      },
      body: JSON.stringify(updateRequest)
      })
      .then(res => res.json())
      .then((json) => {
        this.setState({
          response: json
        });
        return json;
      });
    }

    render() {
        const { toggleEdit, email, username, password, oldPassword, response} = this.state;
        return (
          <Container>
            <Row className='profileRow'>
              <Col sm="12" md={{ size: 6, offset: 3 }}>
                <Form onSubmit={this.updateSubmit}>
                  <h2> Profile </h2>
                  {
                    response ? <p>{response}</p> : null
                  }
                  <FormGroup>
                    <Label for="userName">Username</Label>
                    <Input disabled={!toggleEdit} type="textfield" name="username" id="username" value={username} onChange={e => this.handleUsernameChange(e.target.value)}/>
                  </FormGroup>
                  <FormGroup>
                    <Label for="email">Email</Label>
                    <Input disabled={!toggleEdit} type="email" name="email" id="email" value={email} onChange={e => this.handleEmailChange(e.target.value)}/>
                  </FormGroup>
                  {toggleEdit ?
                  <div>
                    <FormGroup>
                      <Label for="password">New Password</Label>
                      <Input disabled={!toggleEdit} type="password" name="password" id="password" value={password} onChange={e => this.handlePasswordChange(e.target.value)}/>
                    </FormGroup>
                    <FormGroup>
                      <Label for="password">Old Password</Label>
                      <Input disabled={!toggleEdit} type="password" name="password" id="password" value={oldPassword} onChange={e => this.handleOldPasswordChange(e.target.value)}/>
                    </FormGroup>

                    <div className="updateButtonProfile">
                      <Button onClick={this.toggleEdit}>Cancel</Button>
                      <Button onClick={this.submit} >Update</Button>
                    </div>
                  </div>
                  : <a style={{cursor: 'pointer'}, {color: '#0056b3'}} onClick={this.toggleEdit}>Click me to edit</a>}
                </Form>
                </Col>
              </Row>
          </Container>
        );
      }
  }
export default withRouter(withCookies(EditProfile));

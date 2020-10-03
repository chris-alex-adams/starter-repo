import React, { Component } from 'react';
import { withCookies, Cookies } from 'react-cookie';
import { instanceOf } from 'prop-types';
import { withRouter } from "react-router-dom";

class HomePage extends React.Component {

  static propTypes = {
    cookies: instanceOf(Cookies).isRequired
  };

}

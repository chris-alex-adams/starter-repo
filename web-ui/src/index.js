import 'bootstrap/dist/css/bootstrap.min.css';
import React from 'react';
import ReactDOM from 'react-dom';
import './index.css';
import Home from './containers/Home';
import * as serviceWorker from './utils/serviceWorker';
import { CookiesProvider } from 'react-cookie';
import { BrowserRouter } from 'react-router-dom';

ReactDOM.render(
  <CookiesProvider>
    <BrowserRouter>
      <Home />
    </BrowserRouter>
  </CookiesProvider>
  , document.getElementById('root')
);

// If you want your app to work offline and load faster, you can change
// unregister() to register() below. Note this comes with some pitfalls.
// Learn more about service workers: http://bit.ly/CRA-PWA
serviceWorker.register();

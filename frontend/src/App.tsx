import React, { FC } from 'react';
import { Redirect, Route, Switch } from 'react-router-dom';
import CreateRoom from './components/pages/CreateRoom';
import Home from './components/pages/Home';

const App: FC = () => (
  <div className="App">
    <Switch>
      <Route path="/create" component={CreateRoom} />
      <Route path="/" component={Home} />
      <Redirect to="/" />
    </Switch>
  </div>
);

export default App;

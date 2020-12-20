import React, { FC } from 'react';
import { Redirect, Route, Switch } from 'react-router-dom';
import RoomCreator from './components/pages/RoomCreator';
import Home from './components/pages/Home';
import Header from './components/organisms/Header';
import Room from './components/pages/Room';

const App: FC = () => (
  <div className="App">
    <Header />
    <Switch>
      <Route path="/create" component={RoomCreator} />
      <Route path="/room/:roomId" component={Room} />
      <Route path="/" component={Home} />
      <Redirect to="/" />
    </Switch>
  </div>
);

export default App;

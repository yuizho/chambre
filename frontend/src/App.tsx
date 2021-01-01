import React, { FC } from 'react';
import { Redirect, Route, Switch } from 'react-router-dom';
import { RecoilRoot } from 'recoil';
import RoomCreator from './containers/pages/RoomCreator';
import Home from './components/pages/Home';
import Header from './components/organisms/Header';
import Room from './containers/pages/Room';

const App: FC = () => (
  <RecoilRoot>
    <div className="App">
      <Header />
      <Switch>
        <Route path="/create" component={RoomCreator} />
        <Route path="/room/:roomId" component={Room} />
        <Route path="/" component={Home} />
        <Redirect to="/" />
      </Switch>
    </div>
  </RecoilRoot>
);

export default App;

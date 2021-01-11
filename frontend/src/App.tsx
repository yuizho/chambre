import React, { FC } from 'react';
import { Redirect, Route, Switch } from 'react-router-dom';
import {
  MutableSnapshot,
  RecoilRoot,
  useRecoilTransactionObserver_UNSTABLE as useRecoilTransactionObserver,
} from 'recoil';
import RoomCreator from './containers/pages/RoomCreator';
import Home from './components/pages/Home';
import Room from './containers/pages/Room';
import Apply from './containers/pages/Apply';
import { userState } from './states/UserState';
import { eventState } from './states/EventState';

const initializeState = (mutableSnapshot: MutableSnapshot) => {
  const sotredUserSate = localStorage.getItem(userState.key);
  if (sotredUserSate) {
    /* eslint-disable  @typescript-eslint/no-unsafe-member-access */
    mutableSnapshot.set(userState, JSON.parse(sotredUserSate).value);
  }
  const storedEventState = localStorage.getItem(eventState.key);
  if (storedEventState) {
    /* eslint-disable  @typescript-eslint/no-unsafe-member-access */
    mutableSnapshot.set(eventState, JSON.parse(storedEventState).value);
  }
};

const PersistenceObserver: FC = () => {
  useRecoilTransactionObserver(({ snapshot }) => {
    /* eslint-disable  @typescript-eslint/no-unsafe-member-access  */
    /* eslint-disable  @typescript-eslint/no-unsafe-call */
    /* eslint-disable no-restricted-syntax */
    for (const modifiedAtom of (snapshot as any).getNodes_UNSTABLE({
      isModified: true,
    })) {
      const atomLoadable = snapshot.getLoadable(modifiedAtom);
      if (atomLoadable.state === 'hasValue') {
        localStorage.setItem(
          modifiedAtom.key,
          JSON.stringify({ value: atomLoadable.contents }),
        );
      }
    }
  });

  return null;
};

const App: FC = () => (
  <RecoilRoot initializeState={initializeState}>
    <PersistenceObserver />
    <div className="App">
      <Switch>
        <Route path="/create" component={RoomCreator} />
        <Route path="/apply/:roomId" component={Apply} />
        <Route path="/room/:roomId" component={Room} />
        <Route path="/" component={Home} />
        <Redirect to="/" />
      </Switch>
    </div>
  </RecoilRoot>
);

export default App;

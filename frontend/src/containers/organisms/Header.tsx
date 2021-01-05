import React, { FC, useEffect } from 'react';
import { useRecoilState, useRecoilValue } from 'recoil';
import { errorState, loadingState } from '../../states/FetchState';
import HeaderComponent from '../../components/organisms/Header';

const Header: FC = () => {
  const [error, setError] = useRecoilState(errorState);
  const isLoading = useRecoilValue(loadingState);

  useEffect(() => {
    if (!error) {
      return;
    }
    setTimeout(() => setError(undefined), 4000);
  }, [error, setError]);

  return <HeaderComponent {...{ error, setError, isLoading }} />;
};

export default Header;

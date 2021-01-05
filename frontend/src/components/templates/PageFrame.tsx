import { Container } from '@chakra-ui/react';
import React, { FC } from 'react';
import Header from '../../containers/organisms/Header';

const PageFrame: FC = ({ children }) => (
  <>
    <Header />
    <Container maxWidth="85%" mt={4}>
      {children}
    </Container>
  </>
);

export default PageFrame;

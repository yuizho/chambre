import React, { useState } from 'react';
import { Box, Heading, Flex } from '@chakra-ui/react';
import { Link } from 'react-router-dom';
import { useRecoilState, useRecoilValue } from 'recoil';
import { errorState, loadingState } from '../../states/FetchState';
import ErrorAlert from '../atoms/ErrorAlert';
import ProgressBar from '../atoms/ProgressBar';

const Header = () => {
  const [show, setShow] = useState(false);
  const [error, setError] = useRecoilState(errorState);
  const isLoading = useRecoilValue(loadingState);

  return (
    <>
      <Flex
        as="nav"
        align="center"
        justify="space-between"
        wrap="wrap"
        padding="0.8rem"
        bg="teal.500"
        color="white"
      >
        <Flex align="center" mr={5}>
          <Heading as="h1" size="lg" letterSpacing="-.1rem">
            Chambre
          </Heading>
        </Flex>

        <Box
          display={{ base: 'block', md: 'none' }}
          onClick={() => setShow(!show)}
        >
          <svg
            fill="white"
            width="12px"
            viewBox="0 0 20 20"
            xmlns="http://www.w3.org/2000/svg"
          >
            <title>Menu</title>
            <path d="M0 3h20v2H0V3zm0 6h20v2H0V9zm0 6h20v2H0v-2z" />
          </svg>
        </Box>

        <Box
          display={{ base: show ? 'block' : 'none', md: 'flex' }}
          width={{ base: 'full', md: 'auto' }}
          alignItems="center"
          flexGrow={1}
        >
          {/* TODO: component化 */}
          <Box mt={{ base: 4, md: 0 }} mr={6} display="block">
            <Link to="/">Home</Link>
          </Box>
          <Box mt={{ base: 4, md: 0 }} mr={6} display="block">
            <Link to="/create">Create Room</Link>
          </Box>
        </Box>
      </Flex>
      <ProgressBar isLoading={isLoading} />
      <ErrorAlert error={error} onCloseHandler={() => setError(undefined)} />
    </>
  );
};

export default Header;

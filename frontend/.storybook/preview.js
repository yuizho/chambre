import { BrowserRouter } from 'react-router-dom';
import { ChakraProvider } from '@chakra-ui/react';
import { RecoilRoot } from 'recoil';

export const parameters = {
  actions: { argTypesRegex: '^on[A-Z].*' },
};

export const decorators = [
  (Story) => (
    <BrowserRouter>
      <ChakraProvider>
        <RecoilRoot>
          <Story />
        </RecoilRoot>
      </ChakraProvider>
    </BrowserRouter>
  ),
];

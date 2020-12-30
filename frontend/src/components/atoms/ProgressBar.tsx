import { Progress } from '@chakra-ui/react';
import React, { FC } from 'react';

type Prop = {
  isLoading: boolean;
};

const ProgressBar: FC<Prop> = ({ isLoading }) => (
  <>{isLoading && <Progress size="xs" isIndeterminate />}</>
);

export default ProgressBar;

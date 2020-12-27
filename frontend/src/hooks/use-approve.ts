import { useEffect } from 'react';
import fetchApprove from '../api/Approve';

type Props = {
  userId: string;
  userName: string;
};

const useApprove = ({ userId, userName }: Props) => {
  useEffect(() => {
    if (!userId || !userName) {
      return;
    }
    void fetchApprove(userId, userName);
  }, [userId, userName]);
};

export default useApprove;

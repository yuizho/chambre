import { useEffect } from 'react';
import { useHistory } from 'react-router-dom';
import fetchApprove from '../api/Approve';

type Props = {
  userId: string;
  userName: string;
};

const useApprove = ({ userId, userName }: Props) => {
  const history = useHistory();
  useEffect(() => {
    if (!userId || !userName) {
      return;
    }
    void fetchApprove(userId, userName);
  }, [userId, userName, history]);
};

export default useApprove;

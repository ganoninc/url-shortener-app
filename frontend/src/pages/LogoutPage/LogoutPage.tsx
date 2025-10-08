import { useNavigate } from "react-router-dom";
import { useAppDispatch } from "../../hooks/hooks";
import { logoutDueToUserAction } from "../../redux/authSlice";
import { useEffect } from "react";

export default function LogoutPage() {
  const dispatch = useAppDispatch();
  const navigate = useNavigate();

  useEffect(() => {
    dispatch(logoutDueToUserAction());
    navigate("/");
  }, [dispatch, navigate]);

  return <></>;
}

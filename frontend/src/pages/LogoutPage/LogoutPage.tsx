import { useNavigate } from "react-router-dom";
import { useAppDispatch } from "../../hooks/hooks";
import { logout } from "../../redux/authSlice";
import { useEffect } from "react";

export default function LogoutPage() {
  const dispatch = useAppDispatch();
  const navigate = useNavigate();

  useEffect(() => {
    dispatch(logout());
    navigate("/");
  }, [dispatch, navigate]);

  return <></>;
}

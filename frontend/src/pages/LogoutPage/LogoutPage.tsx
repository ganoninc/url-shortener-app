import { useNavigate } from "react-router-dom";
import { useAppDispatch } from "../../hooks/hooks";
import { logoutDueToUserAction } from "../../redux/authSlice";
import { useEffect } from "react";
import toast from "react-hot-toast";

export default function LogoutPage() {
  const dispatch = useAppDispatch();
  const navigate = useNavigate();

  useEffect(() => {
    dispatch(logoutDueToUserAction());
    toast("You’ve been logged out. See you soon!", {
      icon: "👋",
    });
    navigate("/");
  }, [dispatch, navigate]);

  return <></>;
}

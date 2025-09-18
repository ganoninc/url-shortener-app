import { selectStatus } from "../redux/authSlice";
import { useAppSelector } from "./hooks";

export function useLoggedIn() {
  const status = useAppSelector(selectStatus);
  return status === "authenticated";
}

import { selectUser } from "../redux/authSlice";
import { useAppSelector } from "./hooks";

export function useUser() {
  return useAppSelector(selectUser);
}

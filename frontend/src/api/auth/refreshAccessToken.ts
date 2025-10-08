import axios from "axios";
import { apiGatewayUrl } from "../../config/apiGateway";

export async function refreshAccessToken() {
  try {
    const response = await axios.get(
      `${apiGatewayUrl}/auth/refresh-access-token`,
      { withCredentials: true }
    );

    const newToken = response.data as string;

    if (!newToken) {
      throw new Error("No access token returned from refresh endpoint");
    }

    return newToken;
  } catch (error) {
    console.error("Failed to refresh access token", error);
    throw error;
  }
}

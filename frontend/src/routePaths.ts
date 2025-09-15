export const ROUTES = {
  home: "/",
  login: "/login",
  logout: "/logout",
  myUrls: "/my-urls",
  myUrlDetail: (shortId: string = ":shortId") => `/my-urls/${shortId}`,
};

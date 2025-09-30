export const ROUTES = {
  home: "/",
  login: "/login",
  logout: "/logout",
  userShortUrlList: "/my-urls",
  userShortUrlDetail: (shortId: string = ":shortId") => `/my-urls/${shortId}`,
  userNewShortUrlCreated: (shortId: string = ":shortId") =>
    `/my-urls/${shortId}/success`,
};

export function getPortalOrigin() {
  if (typeof window === "undefined") {
    return "http://localhost:5173";
  }
  const { protocol, hostname, port, origin, search } = window.location;
  const queryPortalOrigin = new URLSearchParams(search).get("portal_origin");
  if (queryPortalOrigin) {
    return queryPortalOrigin;
  }
  if (document.referrer) {
    try {
      return new URL(document.referrer).origin;
    } catch {
      // ignore invalid referrer
    }
  }
  const isLocal = hostname === "127.0.0.1" || hostname === "localhost";
  if (isLocal) {
    return `${protocol}//localhost:5173`;
  }
  if (!port || port === "80" || port === "443") {
    return origin;
  }
  return `${protocol}//${hostname}`;
}

export function getSupplierRoot() {
  if (typeof window === "undefined") {
    return "http://127.0.0.1:3180/";
  }
  const base = (import.meta.env.BASE_URL || "/").replace(/\/+$/, "");
  return `${window.location.origin}${base || ""}/`;
}

export function redirectToPortalSso() {
  const redirect = encodeURIComponent(getSupplierRoot());
  window.location.href = `${getPortalOrigin()}/api/sso/login?appCode=supplier&redirect=${redirect}`;
}

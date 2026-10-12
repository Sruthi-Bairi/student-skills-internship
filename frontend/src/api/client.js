const API_BASE = import.meta.env.VITE_API_URL || "https://student-skills-internship.onrender.com";

let csrfToken = "";
let csrfHeader = "X-XSRF-TOKEN";
async function ensureCsrf() {
  const response = await fetch(`${API_BASE}/api/auth/csrf`, {
    method: "GET",
    credentials: "include",
    cache: "no-store",
  });

  if (!response.ok) {
    throw new Error("Could not initialize security token");
  }

  // Read the CSRF token from the cookie set by Spring Security.
  const cookie = document.cookie
    .split("; ")
    .find((item) => item.startsWith("XSRF-TOKEN="));

  if (!cookie) {
    throw new Error(
      "CSRF cookie is missing. Please check the backend configuration."
    );
  }

  csrfToken = decodeURIComponent(cookie.substring("XSRF-TOKEN=".length));
  csrfHeader = "X-XSRF-TOKEN";

  return csrfToken;
}
export async function api(path, options = {}) {
  const method = (options.method || "GET").toUpperCase();
  if (method !== "GET" && method !== "HEAD" && method !== "OPTIONS") {
    await ensureCsrf();
  }

  const headers = {
    ...(options.body ? { "Content-Type": "application/json" } : {}),
    ...(options.headers || {}),
  };
  if (csrfToken && method !== "GET" && method !== "HEAD") {
    headers[csrfHeader] = csrfToken;
  }

  const response = await fetch(`${API_BASE}${path}`, {
    ...options,
    method,
    credentials: "include",
    headers,
  });

  if (response.status === 204) {
    return null;
  }

  const text = await response.text();
  const data = text ? JSON.parse(text) : null;

  if (!response.ok) {
    const message = data?.message || "Request failed";
    const details = data?.details?.length ? ` (${data.details.join(", ")})` : "";
    const error = new Error(message + details);
    error.status = response.status;
    error.body = data;
    throw error;
  }

  return data;
}

export function getApiBase() {
  return API_BASE;
}

let accessToken: string | null = null

export function setAccessToken(token: string | null) {
  accessToken = token
}

export function clearAccessToken() {
  accessToken = null
}

let unauthorizedHandler: (() => void) | null = null
let refreshHandler: (() => Promise<boolean>) | null = null
let refreshing: Promise<boolean> | null = null

export function setUnauthorizedHandler(handler: () => void) {
  unauthorizedHandler = handler
}

export function setRefreshHandler(handler: () => Promise<boolean>) {
  refreshHandler = handler
}

// 동시에 여러 요청이 401을 받아도 refresh는 한 번만 수행한다.
function refreshOnce(): Promise<boolean> {
  if (!refreshHandler) return Promise.resolve(false)
  refreshing ??= refreshHandler()
    .catch(() => false)
    .finally(() => {
      refreshing = null
    })
  return refreshing
}

function requestWithToken(input: RequestInfo | URL, init: RequestInit): Promise<Response> {
  const headers = new Headers(init.headers)
  if (accessToken) headers.set('Authorization', 'Bearer ' + accessToken)
  return fetch(input, { ...init, headers })
}

export async function apiFetch(input: RequestInfo | URL, init: RequestInit = {}): Promise<Response> {
  const hadToken = accessToken !== null
  let response = await requestWithToken(input, init)
  if (response.status !== 401 || !hadToken) return response

  // access token 만료: refresh 토큰으로 재발급 후 1회 재시도, 실패(refresh 만료 포함) 시 로그인 페이지로 이동
  if (await refreshOnce()) {
    response = await requestWithToken(input, init)
    if (response.status !== 401) return response
  }
  unauthorizedHandler?.()
  return response
}

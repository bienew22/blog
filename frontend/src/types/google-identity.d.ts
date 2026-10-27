interface GoogleCredentialResponse {
  credential: string
}

interface GoogleIdentity {
  accounts: {
    id: {
      initialize(options: {
        client_id: string
        callback: (response: GoogleCredentialResponse) => void
      }): void
      renderButton(
        parent: HTMLElement,
        options: { theme: 'outline'; size: 'large'; shape: 'rectangular'; text: 'signin_with'; width: number },
      ): void
    }
  }
}

interface Window {
  google?: GoogleIdentity
}

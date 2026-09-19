export interface Post {
    title: string
    slug: string
    createAt: string
    tags: string[]
}

interface PostsResponse {
    content: Post[]
}

const API_BASE_URL = import.meta.env.VITE_API_BASE_URL

export async function fetchPosts(): Promise<Post[]> {
    const response = await fetch(`${API_BASE_URL}/posts`)
    if (!response.ok) {
        throw new Error(`Failed to fetch posts: ${response.status}`)
    }

    const data: PostsResponse = await response.json()
    return data.content
}

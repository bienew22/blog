export interface Post {
    title: string
    slug: string
    createAt: string
    tags: string[]
}

export interface PostSummary {
    title: string
    slug: string
    createAt: string
}

export async function fetchPosts(): Promise<Post[]> {
    const response = await fetch(`/api/v1/posts`)
    if (!response.ok) {
        throw new Error(`Failed to fetch posts: ${response.status}`)
    }

    const data: Post[] = await response.json()

    return data
}

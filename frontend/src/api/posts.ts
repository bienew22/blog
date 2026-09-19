export interface Post {
    title: string
    slug: string
    createAt: string
    tags: string[]
}

export async function fetchPosts(): Promise<Post[]> {
    const response = await fetch(`/api/articles`)
    if (!response.ok) {
        throw new Error(`Failed to fetch posts: ${response.status}`)
    }

    const data: Post[] = await response.json()

    return data
}

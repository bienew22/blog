import { dummyPost } from "@/data/post"

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

export interface PostDetail {
    title: string
    slug: string
    createAt: string
    content: string
    tags: string[]
}

export async function fetchPosts(): Promise<Post[]> {
    const response = await fetch(`/api/v1/posts`)
    if (!response.ok) {
        throw new Error(`Failed to fetch posts: ${response.status}`)
    }

    const data: Post[] = await response.json()

    return data
}

export async function fetchPostDetail(slug: string): Promise<PostDetail> {
    // const response = await fetch(`/api/v1/posts/${slug}`)
    // if (!response.ok) {
    //     throw new Error(`Failed to fetch post detail: ${response.status}`)
    // }

    // const data: PostDetail = await response.json()
    const data: PostDetail = dummyPost;

    return data
}

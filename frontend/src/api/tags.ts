import { tagPosts } from '@/data/tagPosts'
import type { PostSummary } from './posts'

export interface Tag {
    tagName: string
    postCount: number
}

export interface TagPosts {
    tagName: string
    tagDesc?: string
    postCount: number
    posts: PostSummary[]
}

export async function fetchTags(): Promise<Tag[]> {
    const response = await fetch(`/api/v1/tags`)
    if (!response.ok) {
        throw new Error(`Failed to fetch tags: ${response.status}`)
    }

    const data: Tag[] = await response.json()

    return data
}

export async function fetchTagPosts(tagName: string): Promise<TagPosts | null> {
    // const response = await fetch(`/api/v1/tags/${tagName}`)
    // if (response.status === 404) {
    //     return null
    // }
    // if (!response.ok) {
    //     throw new Error(`Failed to fetch tag posts: ${response.status}`)
    // }

    // const data: TagPosts = await response.json()
    const data: TagPosts = tagPosts

    console.log(tagName);

    return data
}
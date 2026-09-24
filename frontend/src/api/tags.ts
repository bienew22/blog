export interface Tag {
    tagName: string
    postCount: number
}

export async function fetchTags(): Promise<Tag[]> {
    const response = await fetch(`/api/v1/tags`)
    if (!response.ok) {
        throw new Error(`Failed to fetch tags: ${response.status}`)
    }

    const data: Tag[] = await response.json()

    return data
}

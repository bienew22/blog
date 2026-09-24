export interface Tag {
    tagName: string
    postCount: number
}

export async function fetchTags(): Promise<Tag[]> {
    const response = await fetch(`https://962b0b59-0ec0-476d-8444-d3dee0068b39.mock.pstmn.io/api/tags`)
    if (!response.ok) {
        throw new Error(`Failed to fetch tags: ${response.status}`)
    }

    const data: Tag[] = await response.json()

    return data
}

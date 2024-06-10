package dev.traydr.geef.domain.service

import dev.traydr.geef.domain.Post
import dev.traydr.geef.domain.repository.PostRepository

class PostService(private val postRepository: PostRepository) {
    fun createPost(post: Post): Long {
        return postRepository.create(post)
    }

    fun getAllPostsByUser(userId: Long): List<Post> {
        println("\n\n\n All Posts by user $userId")
        return postRepository.findAllPostsByUserId(userId)
    }

    fun getPostById(id: Long): Post? {
        return postRepository.findPostById(id)
    }
}
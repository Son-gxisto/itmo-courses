<template>
  <div>
    <PostF v-for="post in posts" :post="post" :key="post.id"/>
  </div>
</template>

<script>
import PostF from "./PostF";

export default {
  name: "Index",
  props: ["posts"],
  components: {
    PostF
  }
}
</script>

<style scoped>

</style>
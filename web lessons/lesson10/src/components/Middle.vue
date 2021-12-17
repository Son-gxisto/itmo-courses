<template>
    <div class="middle">
        <main>
            <Index v-if="page === 'Index'"/>
        </main>
    </div>
</template>

<script>
import Index from "@/components/middle/Index";

export default {
    name: "Middle",
    data: function () {
        return {
            page: "Index"
        }
    },
    components: {
      Index
    },
    props: ["posts", "users"],
    beforeCreate() {
        this.$root.$on("onChangePage", (page) => this.page = page)
    }
}
</script>

<style scoped>

</style>
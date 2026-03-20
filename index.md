---
layout: home
---

<!-- 个人档案 Hero 区域：头像 + 欢迎语 -->
<div class="profile-hero">
  <img
    src="https://github.com/sheng-7-hys.png"
    alt="Sheng's Avatar"
    class="profile-avatar"
  >
  <div class="profile-welcome">
    <h1>你好，我是 Sheng 👋</h1>
    <p>记录生活、技术与思考</p>
  </div>
</div>

<!-- 文章列表区域 -->
<section class="post-list-section">
  <h2 class="post-list-section__title">📝 近期文章</h2>
  {% if site.posts.size > 0 %}
  <ul class="post-list-cards">
    {% for post in site.posts limit:10 %}
    <li class="post-card">
      <a class="post-card__link" href="{{ post.url | relative_url }}">
        <span class="post-card__title">{{ post.title | escape }}</span>
        <time class="post-card__date" datetime="{{ post.date | date_to_xmlschema }}">
          {{ post.date | date: "%Y-%m-%d" }}
        </time>
      </a>
    </li>
    {% endfor %}
  </ul>
  {% else %}
  <p class="post-list-empty">暂时还没有文章，敬请期待 ✨</p>
  {% endif %}
</section>

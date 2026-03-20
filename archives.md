---
layout: page
title: 文章归档
permalink: /archives/
---

<div class="archives-list">
{% assign posts_by_year = site.posts | group_by_exp: "post", "post.date | date: '%Y'" %}
{% for year_group in posts_by_year %}
  <h2 class="archive-year">{{ year_group.name }}</h2>
  <ul class="archive-posts">
    {% for post in year_group.items %}
    <li class="archive-post-item">
      <time class="archive-date">{{ post.date | date: "%m-%d" }}</time>
      <a href="{{ post.url | relative_url }}">{{ post.title | escape }}</a>
    </li>
    {% endfor %}
  </ul>
{% endfor %}
{% if site.posts.size == 0 %}
  <p style="color:#bbb;text-align:center;padding:24px 0;">暂无文章 ✨</p>
{% endif %}
</div>

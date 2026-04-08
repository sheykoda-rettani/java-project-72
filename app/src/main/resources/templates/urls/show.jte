@import hexlet.code.dto.UrlPage

@import static hexlet.code.util.ViewUtils.PAGE_DATE_FORMATTER

@param UrlPage urlPage

@template.layout.layout(
    page = urlPage,
    content = @`
        <h1 class="display-4">Информация о сайте</h1>
        <a href="/urls" class="btn btn-secondary mb-3">Назад к списку</a>

        <table class="table" data-test="url">
            <tbody>
                <tr>
                    <td>ID</td>
                    <td>${urlPage.getUrl().getId()}</td>
                </tr>
                <tr>
                    <td>Имя</td>
                    <td>${urlPage.getUrl().getName()}</td>
                </tr>
                <tr>
                    <td>Дата создания</td>
                    <td>${urlPage.getUrl().getCreatedAt().format(PAGE_DATE_FORMATTER)}</td>
                </tr>
            </tbody>
        </table>
    `
)
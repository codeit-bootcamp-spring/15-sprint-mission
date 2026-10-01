const USER_API = '/api/user/findAll';
const BINARY_CONTENT_API = '/api/binaryContent/find';

const userList = document.getElementById('user-list');

// 사용자 목록 불러오기
async function loadUsers() {
    try {
        const response = await fetch(USER_API);

        if (!response.ok) {
            throw new Error('사용자 목록을 불러오지 못했습니다.');
        }

        const users = await response.json();

        userList.innerHTML = '';

        for (const user of users) {
            const card = await createUserCard(user);
            userList.appendChild(card);
        }

    } catch (error) {
        console.error(error);

        userList.innerHTML = `
            <p class="error-message">
                사용자 목록을 불러오는 중 오류가 발생했습니다.
            </p>
        `;
    }
}

// 사용자 카드 만들기
async function createUserCard(user) {
    const card = document.createElement('article');
    card.className = 'user-card';

    const profileImage = document.createElement('img');
    profileImage.className = 'profile-image';
    profileImage.alt = `${user.username} 프로필 이미지`;

    // 기본 이미지
    profileImage.src = createDefaultProfile();

    // 프로필이 존재하면 BinaryContent API로 이미지 조회
    if (user.profileId) {
        try {
            const imageSrc = await loadProfileImage(user.profileId);

            if (imageSrc) {
                profileImage.src = imageSrc;
            }
        } catch (error) {
            console.error(
                `${user.username} 프로필 이미지 조회 실패`,
                error
            );
        }
    }

    const info = document.createElement('div');
    info.className = 'user-info';

    const name = document.createElement('h2');
    name.className = 'user-name';
    name.textContent = user.username;

    const email = document.createElement('p');
    email.className = 'user-email';
    email.textContent = user.email;

    const status = document.createElement('span');
    status.className = user.online
        ? 'status online'
        : 'status offline';

    status.textContent = user.online
        ? '온라인'
        : '오프라인';

    info.appendChild(name);
    info.appendChild(email);
    info.appendChild(status);

    card.appendChild(profileImage);
    card.appendChild(info);

    return card;
}

// BinaryContent 조회 후 이미지로 변환
async function loadProfileImage(binaryContentId) {
    const response = await fetch(
        `${BINARY_CONTENT_API}?binaryContentId=${binaryContentId}`
    );

    if (!response.ok) {
        throw new Error('프로필 이미지를 불러오지 못했습니다.');
    }

    const binaryContent = await response.json();

    if (!binaryContent.bytes) {
        return null;
    }

    return `data:${binaryContent.contentType};base64,${binaryContent.bytes}`;
}

// 프로필이 없을 경우 사용하는 기본 이미지
function createDefaultProfile() {
    const svg = `
        <svg xmlns="http://www.w3.org/2000/svg"
             width="200"
             height="200"
             viewBox="0 0 200 200">

            <rect width="200"
                  height="200"
                  rx="100"
                  fill="#f2edff"/>

            <text x="100"
                  y="125"
                  text-anchor="middle"
                  font-size="85">
                🐶
            </text>
        </svg>
    `;

    return `data:image/svg+xml;charset=UTF-8,${encodeURIComponent(svg)}`;
}

// 페이지가 열리면 사용자 목록 조회
loadUsers();
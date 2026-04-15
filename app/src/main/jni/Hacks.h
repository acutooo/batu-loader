#ifndef BETA_ESP_IMPORTANT_HACKS_H
#define BETA_ESP_IMPORTANT_HACKS_H

#include "socket.h"
#include "Color.h"
#include "items.h"
#include "rLogin/Login.h"
#include "Vector3.hpp"
#include <cmath>
#include <string>
#include <vector>
#include <cstdio>

// ==========================================
// ГЛОБАЛЬНЫЕ ПЕРЕМЕННЫЕ И НАСТРОЙКИ
// ==========================================

Color clrEnemy, clrEdge, clrBox, clrAlert, clr, clrTeam, clrDist, clrHealth, clrText, grenadeColor;
Options options{1, -1, -1, 3, false, false, 1, false, 200, 200, 200, 19, 19, -1, false};
OtherFeature otherFeature{false, false, false, false};

bool isPlayerTeamID = false;
bool isRadar = false;
bool isLootItems = false;

int botCount = 0;
int playerCount = 0;
Response response;
Request request;

// Буферы
char extra[64];
char text[128];

// Переменные анимации
static float globalTime = 0.0f;
static float pulseVal = 0.0f;
static float rainbowHue = 0.0f;

// ==========================================
// ВСПОМОГАТЕЛЬНЫЕ МАТЕМАТИЧЕСКИЕ ФУНКЦИИ
// ==========================================

namespace Utils {
    float DegToRad(float deg) {
        return deg * (M_PI / 180.0f);
    }

    // Плавная радуга
    Color Rainbow(float speed = 0.05f) {
        rainbowHue += speed;
        if (rainbowHue > 1.0f) rainbowHue = 0.0f;

        float h = rainbowHue * 6.0f;
        float f = h - (int)h;
        float q = 1.0f - f;

        float r = 0, g = 0, b = 0;
        switch ((int)h % 6) {
            case 0: r = 1; g = f; b = 0; break;
            case 1: r = q; g = 1; b = 0; break;
            case 2: r = 0; g = 1; b = f; break;
            case 3: r = 0; g = q; b = 1; break;
            case 4: r = f; g = 0; b = 1; break;
            default: r = 1; g = 0; b = q; break;
        }
        return Color((int)(r * 255), (int)(g * 255), (int)(b * 255), 255);
    }

    // Пульсирующий цвет (например, для алертов)
    Color PulseRed(float speed = 0.1f) {
        pulseVal += speed;
        float alpha = (sin(pulseVal) + 1.0f) * 0.5f * 155 + 100;
        return Color(255, 50, 50, (int)alpha);
    }

    // Цвет дистанции
    Color GetDistanceColor(float dist) {
        if (dist < 50.0f) return Color(255, 40, 40, 255); // Опасно
        if (dist < 150.0f) return Color(255, 165, 0, 255); // Средне
        return Color(0, 255, 100, 255); // Далеко
    }

    // Цвет здоровья
    Color GetHealthColor(int hp) {
        if (hp > 100) hp = 100;
        if (hp < 0) hp = 0;
        int r = (int)((100 - hp) * 2.55f);
        int g = (int)(hp * 2.55f);
        return Color(r, g, 0, 255);
    }

    bool IsAimingAtMe(int status) {
        return (status == 520 || status == 544 || status == 656 || status == 521 || status == 528 || status == 3145736);
    }
}

// ==========================================
// ФУНКЦИИ ОТРИСОВКИ (VISUALS)
// ==========================================

namespace Draw {

    void TextStroke(ESP& esp, const char* str, Vec2 pos, float size, Color color, bool center = true) {
        float offset = 1.0f;
        esp.DrawText(Color(0, 0, 0, 255), str, Vec2(pos.x - offset, pos.y), size);
        esp.DrawText(Color(0, 0, 0, 255), str, Vec2(pos.x + offset, pos.y), size);
        esp.DrawText(Color(0, 0, 0, 255), str, Vec2(pos.x, pos.y - offset), size);
        esp.DrawText(Color(0, 0, 0, 255), str, Vec2(pos.x, pos.y + offset), size);
        esp.DrawText(color, str, pos, size);
    }

    void CornerBox(ESP& esp, float x, float y, float w, float h, float tickness, Color color) {
        float lineW = w / 4.0f;
        float lineH = h / 6.0f;

        esp.DrawLine(color, tickness, Vec2(x, y), Vec2(x + lineW, y));
        esp.DrawLine(color, tickness, Vec2(x, y), Vec2(x, y + lineH));

        esp.DrawLine(color, tickness, Vec2(x + w, y), Vec2(x + w - lineW, y));
        esp.DrawLine(color, tickness, Vec2(x + w, y), Vec2(x + w, y + lineH));

        esp.DrawLine(color, tickness, Vec2(x, y + h), Vec2(x + lineW, y + h));
        esp.DrawLine(color, tickness, Vec2(x, y + h), Vec2(x, y + h - lineH));

        esp.DrawLine(color, tickness, Vec2(x + w, y + h), Vec2(x + w - lineW, y + h));
        esp.DrawLine(color, tickness, Vec2(x + w, y + h), Vec2(x + w, y + h - lineH));
    }

    void DrawHUD(ESP& esp, int screenW, int screenH, int pCount, int bCount) {
        float centerX = screenW / 2.0f;
        float fontSize = screenH / 45.0f;

        esp.DrawFilledRect(Color(10, 10, 10, 200),
                           Vec2(centerX - 150, 20),
                           Vec2(centerX + 150, 70));

        esp.DrawLine(Utils::Rainbow(0.02f), 2.0f,
                     Vec2(centerX - 150, 70),
                     Vec2(centerX + 150, 70));

        char infoBuf[64];
        sprintf(infoBuf, "ENEMIES: %d  |  BOTS: %d", pCount, bCount);

        Color statusClr = (pCount > 0) ? Color(255, 50, 50, 255) : Color(0, 255, 100, 255);
        TextStroke(esp, infoBuf, Vec2(centerX - 90, 40), fontSize, statusClr);

        TextStroke(esp, "Buy Source: @BatuX28", Vec2(centerX - 50, 25), fontSize * 0.8f, Color(255, 255, 255, 150));

        if (pCount > 3) {
            TextStroke(esp, "YÜKSEK RİSKLİ BÖLGE", Vec2(centerX - 60, 80), fontSize, Utils::PulseRed());
        }
    }

    // ИСПРАВЛЕНО: Убран лишний аргумент thickness у DrawFilledCircle
    void DrawRadar(ESP& esp, Vec2 radarPos, float radarSize, Vec2 enemyPos, Color color, float angle) {
        esp.DrawFilledCircle(Color(0, 0, 0, 150), radarPos, radarSize);
        esp.DrawCircle(Color(255, 255, 255, 200), radarPos, radarSize, 1.5f);

        esp.DrawLine(Color(255, 255, 255, 50), 1.0f, Vec2(radarPos.x - radarSize, radarPos.y), Vec2(radarPos.x + radarSize, radarPos.y));
        esp.DrawLine(Color(255, 255, 255, 50), 1.0f, Vec2(radarPos.x, radarPos.y - radarSize), Vec2(radarPos.x, radarPos.y + radarSize));

        // ИСПРАВЛЕНО: Убран лишний аргумент thickness
        esp.DrawFilledCircle(color, enemyPos, 4.0f);
    }
}

// ==========================================
// ОСНОВНАЯ ЛОГИКА ESP
// ==========================================

void DrawESP(ESP esp, int screenWidth, int screenHeight) {
    if (!xConnected && !xServerConnection) return;

    request.ScreenHeight = screenHeight;
    request.ScreenWidth = screenWidth;
    request.options = options;
    request.otherFeature = otherFeature;
    request.Mode = InitMode;

    send((void *) &request, sizeof(request));
    receive((void *) &response);

    if (!response.Success) return;

    globalTime += 0.1f;
    float mScaleY = screenHeight / 1080.0f;
    float skelThick = 1.5f * mScaleY;
    float textSize = screenHeight / 50.0f;

    playerCount = 0;
    botCount = 0;
    for (int i = 0; i < response.PlayerCount; i++) {
        if (response.Players[i].isBot) botCount++;
        else playerCount++;
    }

    Draw::DrawHUD(esp, screenWidth, screenHeight, playerCount, botCount);

    for (int i = 0; i < response.PlayerCount; i++) {
        PlayerData* p = &response.Players[i];

        if (p->HeadLocation.z == 1.0f || p->HeadLocation.x < -50 || p->HeadLocation.x > screenWidth + 50)
            continue;

        float x = p->HeadLocation.x;
        float y = p->HeadLocation.y;
        float dist = p->Distance;

        float magic_number = (dist * response.fov);
        if (magic_number <= 0) magic_number = 1;

        float boxHeight = (screenWidth / 1.5f) / magic_number;
        float boxWidth = (screenWidth / 3.0f) / magic_number;

        float top = y - boxHeight * 0.2f;
        float bottom = y + boxHeight * 0.8f;
        float left = x - boxWidth / 2.0f;

        Color mainColor;
        if (p->isBot) {
            mainColor = Color(200, 200, 200, 255);
        } else {
            if (p->isVisible) mainColor = Color(0, 255, 0, 255);
            else mainColor = Color(255, 0, 0, 255);
        }

        // 1. ЛИНИЯ
        if (isPlayerLine) {
            esp.DrawLine(Color(mainColor.r, mainColor.g, mainColor.b, 100), 1.5f,
                         Vec2(screenWidth / 2, screenHeight),
                         Vec2(x, bottom));
        }

        // 2. СКЕЛЕТ
        if (isSkeleton && p->Bone.isBone) {
            Color boneColor = p->isBot ? Color(255, 255, 255, 180) : Color(255, 215, 0, 200);

            esp.DrawLine(boneColor, skelThick, Vec2(p->Bone.neck.x, p->Bone.neck.y), Vec2(p->Bone.cheast.x, p->Bone.cheast.y));
            esp.DrawLine(boneColor, skelThick, Vec2(p->Bone.cheast.x, p->Bone.cheast.y), Vec2(p->Bone.pelvis.x, p->Bone.pelvis.y));

            esp.DrawLine(boneColor, skelThick, Vec2(p->Bone.neck.x, p->Bone.neck.y), Vec2(p->Bone.lSh.x, p->Bone.lSh.y));
            esp.DrawLine(boneColor, skelThick, Vec2(p->Bone.lSh.x, p->Bone.lSh.y), Vec2(p->Bone.lElb.x, p->Bone.lElb.y));
            esp.DrawLine(boneColor, skelThick, Vec2(p->Bone.lElb.x, p->Bone.lElb.y), Vec2(p->Bone.lWr.x, p->Bone.lWr.y));

            esp.DrawLine(boneColor, skelThick, Vec2(p->Bone.neck.x, p->Bone.neck.y), Vec2(p->Bone.rSh.x, p->Bone.rSh.y));
            esp.DrawLine(boneColor, skelThick, Vec2(p->Bone.rSh.x, p->Bone.rSh.y), Vec2(p->Bone.rElb.x, p->Bone.rElb.y));
            esp.DrawLine(boneColor, skelThick, Vec2(p->Bone.rElb.x, p->Bone.rElb.y), Vec2(p->Bone.rWr.x, p->Bone.rWr.y));

            esp.DrawLine(boneColor, skelThick, Vec2(p->Bone.pelvis.x, p->Bone.pelvis.y), Vec2(p->Bone.lTh.x, p->Bone.lTh.y));
            esp.DrawLine(boneColor, skelThick, Vec2(p->Bone.lTh.x, p->Bone.lTh.y), Vec2(p->Bone.lKn.x, p->Bone.lKn.y));
            esp.DrawLine(boneColor, skelThick, Vec2(p->Bone.lKn.x, p->Bone.lKn.y), Vec2(p->Bone.lAn.x, p->Bone.lAn.y));

            esp.DrawLine(boneColor, skelThick, Vec2(p->Bone.pelvis.x, p->Bone.pelvis.y), Vec2(p->Bone.rTh.x, p->Bone.rTh.y));
            esp.DrawLine(boneColor, skelThick, Vec2(p->Bone.rTh.x, p->Bone.rTh.y), Vec2(p->Bone.rKn.x, p->Bone.rKn.y));
            esp.DrawLine(boneColor, skelThick, Vec2(p->Bone.rKn.x, p->Bone.rKn.y), Vec2(p->Bone.rAn.x, p->Bone.rAn.y));

            float headRad = (screenHeight / 12.0f) / magic_number;
            esp.DrawCircle(boneColor, Vec2(x, y), headRad, skelThick);
        }

        // 3. КОРОБКА
        if (isPlayerBox) {
            Draw::CornerBox(esp, left, top, boxWidth, boxHeight, 2.0f, mainColor);
        }

        // 4. ИНФОРМАЦИЯ
        if (isPlayerName || isPlayerDistance || isPlayerWeapon) {
            float textY = top - 5;

            if (isPlayerName) {
                char nameBuf[64];
                if (p->isBot) sprintf(nameBuf, "[BOT]");
                else {
                    std::string safeName(reinterpret_cast<char*>(p->PlayerNameByte), 15);
                    sprintf(nameBuf, "%s", safeName.c_str());
                }
                Draw::TextStroke(esp, nameBuf, Vec2(x, textY), textSize, Color::White(255));
                textY -= textSize;
            }

            if (isPlayerDistance) {
                char distBuf[32];
                sprintf(distBuf, "[ %0.0f M ]", p->Distance);
                Color distColor = Utils::GetDistanceColor(p->Distance);
                Draw::TextStroke(esp, distBuf, Vec2(x, bottom + 5), textSize * 0.9f, distColor);
            }

            if (isPlayerWeapon && p->Weapon.isWeapon) {
                esp.DrawWeapon(Color(255, 240, 200), p->Weapon.id, p->Weapon.ammo, p->Weapon.ammo,
                               Vec2(x, bottom + 25), textSize);
            }

            if (isPlayerTeamID) {
                char teamBuf[16];
                sprintf(teamBuf, "ID:%d", p->TeamID);
                Draw::TextStroke(esp, teamBuf, Vec2(left - 25, top), textSize * 0.8f, Color(100, 200, 255));
            }
        }

        // 5. ЗДОРОВЬЕ
        if (isPlayerHealth) {
            float barW = 4.0f;
            float barH = boxHeight;
            float barX = left - 6.0f;
            float barY = top;

            float hpPercent = p->Health / 100.0f;
            if (hpPercent < 0) hpPercent = 0;
            if (hpPercent > 1) hpPercent = 1;

            float fillH = barH * hpPercent;

            esp.DrawFilledRect(Color(0, 0, 0, 150), Vec2(barX, barY), Vec2(barX + barW, barY + barH));
            Color hpColor = Utils::GetHealthColor(p->Health);
            esp.DrawFilledRect(hpColor, Vec2(barX + 1, barY + (barH - fillH)), Vec2(barX + barW - 1, barY + barH));
            esp.DrawRect(Color(0, 0, 0, 255), 1.0f, Vec2(barX, barY), Vec2(barX + barW, barY + barH));

            if (p->Health == 0) {
                Draw::TextStroke(esp, "KNOCK", Vec2(x, top - 30), textSize, Color(255, 0, 0));
            }
        }

        // 6. WARNING
        if (p->isVisible && Utils::IsAimingAtMe(p->StatusPlayer)) {
            esp.DrawLine(Utils::PulseRed(), 2.0f, Vec2(screenWidth/2, screenHeight/2), Vec2(x, y));
            Draw::TextStroke(esp, "!!! UYARI !!!", Vec2(screenWidth/2, screenHeight/3), textSize * 2.0f, Utils::PulseRed());
        }

        // 7. РАДАР
        if (isRadar) {
            Draw::DrawRadar(esp, request.radarPos, request.radarSize, p->RadarLocation, mainColor, 0);
        }
    }

    // ГРАНАТЫ
    for (int i = 0; i < response.GrenadeCount; i++) {
        GrenadeData* g = &response.Grenade[i];
        if (!isGrenadeWarning || g->Location.z == 1.0f) continue;

        const char* gName = "GRENADE";
        Color gColor = Color::White(255);

        switch (g->type) {
            case 1: gColor = Color(255, 0, 0); gName = "FRAG"; break;
            case 2: gColor = Color(255, 140, 0); gName = "MOLOTOF"; break;
            case 3: gColor = Color(255, 255, 0); gName = "ŞAŞIRTMA"; break;
            default: gName = "SİS"; break;
        }

        esp.DrawCircle(gColor, Vec2(g->Location.x, g->Location.y), 20.0f, 2.0f);
        sprintf(extra, "%s [%0.0fm]", gName, g->Distance);
        Draw::TextStroke(esp, extra, Vec2(g->Location.x, g->Location.y - 25), textSize, gColor);

        if (g->Distance < 15.0f && (g->type == 1 || g->type == 2)) {
            Draw::TextStroke(esp, "TEHLİKE BOMBASI", Vec2(screenWidth/2, screenHeight/2 + 100), textSize * 1.5f, Utils::PulseRed());
            esp.DrawLine(gColor, 2.0f, Vec2(screenWidth/2, screenHeight/2 + 120), Vec2(g->Location.x, g->Location.y));
        }
    }

    // ТРАНСПОРТ
    if (isVehicles) {
        for (int i = 0; i < response.VehicleCount; i++) {
            VehicleData* v = &response.Vehicles[i];
            if (v->Location.z == 1.0f) continue;

            esp.DrawVehicles(v->VehicleName, v->Distance, v->Health, v->Fuel, Vec2(v->Location.x, v->Location.y), textSize);
        }
    }

    // ЛУТ
    if (isItems) {
        for (int i = 0; i < response.ItemsCount; i++) {
            ItemData* item = &response.Items[i];
            if (item->Location.z == 1.0f) continue;
            esp.DrawItems(item->ItemName, item->Distance, Vec2(item->Location.x, item->Location.y), textSize * 0.8f);
        }
    }

    // ЛУТБОКСЫ
    if (isLootItems) {
        for (int i = 0; i < response.BoxItemsCount; i++) {
            if (response.BoxItems[i].Location.z == 1.0f) continue;
            BoxItemData* box = &response.BoxItems[i];

            esp.DrawFilledRect(Color(0, 0, 0, 100),
                               Vec2(box->Location.x - 20, box->Location.y - 10),
                               Vec2(box->Location.x + 20, box->Location.y + 10));
            Draw::TextStroke(esp, "DEAD BOX", Vec2(box->Location.x, box->Location.y), textSize * 0.8f, Color(255, 215, 0));

            char* iname;
            for(int j=0; j<box->itemCount && j < 5; j++) {
                if(GetBox((int)box->itemID[j], &iname)) {
                    // ИСПРАВЛЕНО: Добавлен аргумент 255 к Color::White
                    esp.DrawText(Color::White(255), iname, Vec2(box->Location.x, box->Location.y + 15 + (j*15)), textSize*0.7f);
                }
            }
        }
    }

    if (options.openState != 0 && options.aimBullet != 0) {
        float centerX = screenWidth / 2;
        float centerY = screenHeight / 2;

        Color fovColor = (options.aimT == 0) ? Color(0, 100, 255, 100) : Color(0, 255, 0, 100);
        esp.DrawCircle(fovColor, Vec2(centerX, centerY), options.aimingRange, 1.5f);
    }

    if (options.tracingStatus) {
        float py = screenHeight / 2;
        float px = screenWidth / 2;
        esp.DrawRect(Color(0, 255, 0, 150), 1.0f,
                     Vec2(options.touchY - options.touchSize/2, py*2 - options.touchX + options.touchSize/2),
                     Vec2(options.touchY + options.touchSize/2, py*2 - options.touchX - options.touchSize/2));
    }
}

#endif // BETA_ESP_IMPORTANT_HACKS_H
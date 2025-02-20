using System.Collections;
using System.Collections.Generic;
using UnityEngine;

public class HP: MonoBehaviour
{
    public int hp = 10;


    public void Damage(int dmg)
    {
        hp -= dmg;
        if (hp <= 0)
        {
            Destroy(gameObject);
        }
    }
}
